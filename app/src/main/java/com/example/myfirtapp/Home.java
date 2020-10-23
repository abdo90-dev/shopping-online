package com.example.myfirtapp;

import android.content.Intent;
import android.os.Bundle;
import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.adapter.MyAdapter;
import com.example.myfirtapp.model.Products;
import com.example.myfirtapp.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home<List> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
      DatabaseReference setQuery;
      RecyclerView recyclerView;
      RecyclerView.LayoutManager layoutManager;
      String intentS;
    FirebaseRecyclerOptions<Products> options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
         recyclerView = findViewById(R.id.recyclerView1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        intentS = getIntent().getStringExtra("admin");
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent intent = new Intent(Home.this,CartItem.class);
           intent.putExtra("PID",intentS);
           startActivity(intent);
            }
        });

        setQuery = FirebaseDatabase.getInstance().getReference().child("products");
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(setQuery,Products.class).build();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        TextView textView = view.findViewById(R.id.username);
        CircleImageView circleImageView = view.findViewById(R.id.profile_image);
        textView.setText(Prevalent.curentOnlineuser.getUsername());
        Picasso.get().load(Prevalent.curentOnlineuser.getImage()).placeholder(R.drawable.profile).into(circleImageView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.product_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.product_image);
                holder.product_description.setText(model.getDiscreption());
                holder.product_price.setText(model.getPrice());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intentS.equals("admin")){
                            Intent intent = new Intent(Home.this, ProductsOrderDetails.class);
                            intent.putExtra("pId",model.getpId());
                            startActivity(intent);
                        }else{
                        Intent intent = new Intent(Home.this, ProductItem.class);
                        intent.putExtra("pId",model.getpId());
                        startActivity(intent); }
            }});
                }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView search = (androidx.appcompat.widget.SearchView) itemSearch.getActionView();
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFirabase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFirabase(newText);
                return false;
            }
        });
        return true;
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

         if (id == R.id.nav_cart) {
             Intent intent= new Intent(Home.this,CartItem.class);
             intent.putExtra("admin",intentS);
             startActivity(intent);
        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_settings) {
           Intent intent = new Intent(Home.this,Settings.class);
           intent.putExtra("admin",intentS);
           startActivity(intent);
        } else if (id == R.id.nav_logout) {
             Paper.book().destroy();
             Intent intent = new Intent(Home.this,MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

private void searchFirabase(String search){
        String querry = search.toLowerCase();

    Query query = setQuery.orderByChild("name").startAt(querry).endAt(querry + "\uf8ff");
    options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(query,Products.class).build();
    FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter1 = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
            holder.product_name.setText(model.getName());
            Picasso.get().load(model.getImage()).into(holder.product_image);
            holder.product_description.setText(model.getDiscreption());
            holder.product_price.setText(model.getPrice());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, ProductItem.class);
                    intent.putExtra("pId",model.getpId());
                    startActivity(intent);}
                });
        }



        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);

            return new ProductViewHolder(view);
        }
    };
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter1);
    adapter1.startListening();

        }
    }



