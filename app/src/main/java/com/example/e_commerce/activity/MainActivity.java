package com.example.e_commerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.category.Category;
import com.example.e_commerce.category.CategoryDAO;
import com.example.e_commerce.customer.Customer;
import com.example.e_commerce.product.MainAdapter;
import com.example.e_commerce.product.Product;
import com.example.e_commerce.product.ProductDAO;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Customer customer;
    int selectedcat;
    ArrayList<Category> categories;
    ArrayList<Product> products;
    MainAdapter productadapt;
    Spinner categoryspinner;
    GridView productslistview;
    ProductDAO dao;
    MenuItem searchicon;
    SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //if(Build.VERSION.SDK_INT >= 21)
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        initialize();
        LoadCategories();
        LoadProducts();


    }

    private void initialize() {
        customer = (Customer) getIntent().getSerializableExtra("Customer");
        getSupportActionBar().setTitle("");
        categoryspinner = (Spinner) findViewById(R.id.CategoriesSpinner);
        productslistview=(GridView) findViewById(R.id.maingridview);
        dao=new ProductDAO(getApplicationContext());
        selectedcat=-1;
    }

    private void LoadCategories()
    {
        CategoryDAO dao = new CategoryDAO(getApplicationContext());
        categories = dao.getCategories();
        ArrayList<String> cats=new ArrayList<String>();
        cats.add("All");
        for(int i=0;i<categories.size();i++)
        {
            cats.add(categories.get(i).getName());
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, cats);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(adp);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    selectedcat=-1;
                else
                    selectedcat=categories.get(position-1).getID();
                LoadProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void LoadProducts() {
        if(selectedcat==-1)
            products = dao.getProducts();
        else
            products=dao.getProducts(selectedcat);
        productadapt = new MainAdapter(getApplicationContext(), R.layout.product, products);
        productslistview.setAdapter(productadapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        searchicon = menu.findItem(R.id.search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchview = (SearchView) searchicon.getActionView();

            searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchview.setIconifiedByDefault(true);

            ImageView closeButton = (ImageView) searchview.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EditText et = (EditText) findViewById(R.id.search_src_text);
                    et.setText("");
                    searchview.setQuery("", false);
                    searchview.onActionViewCollapsed();

                    searchicon.collapseActionView();
                    searchview.setIconifiedByDefault(true);
                    LoadProducts();
                }
            });
        }

        /*
        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        searchicon.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Nothing to do here
                //LoggerUtils.d(LOG, "Search widget expand ");
                return true; // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //LoggerUtils.d(LOG, "Search widget colapsed ");
                return true; // Return true to collapse action view
            }
        });*/

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(int i=0;i<products.size();)
                {
                    if(products.get(i).getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                        i++;
                    else
                        productadapt.remove(products.get(i));
                }
                productadapt.notifyDataSetChanged();
                searchview.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            //case R.id.search:
                //return true;
            case R.id.MicSearch:        return micSearch();
            case R.id.BarcodeSearch:    return barcodeSearch();
            case R.id.ProfileItem:      return openProfile();
            case R.id.AddItem:          return addProduct();
            case R.id.ShopingCart_Icon: return openCart();
            case R.id.Logout:           return logout();
        }
        return false;
    }

    public boolean micSearch()
    {
        LoadProducts();
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(i, 1);
        return true;
    }

    public boolean barcodeSearch()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
        return true;
    }

    public boolean openProfile()
    {
        return true;
    }

    public boolean addProduct()
    {
        Intent i = new Intent(MainActivity.this,AddProductActivity.class);
        i.putExtra("Cats", categories);
        startActivity(i);
        return true;
    }

    public boolean openCart()
    {
        Intent i = new Intent(MainActivity.this, CartActivity.class);
        i.putExtra("SelectedCounts", productadapt.getSelectedcount());
        i.putExtra("SelectedProducts", productadapt.getSelectedproducts());
        i.putExtra("CustomerID",customer.getID());
        startActivity(i);
        return true;
    }

    public boolean logout()
    {
        SharedPreferences preferences = getSharedPreferences("rememberme",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("remember",false);
        editor.putInt("ID",-1);
        editor.apply();

        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String query = null;
        if (intentResult != null)
        {
            if (intentResult.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                query = (intentResult.getContents());
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            query = text.get(0);
        }

        if(query != null) {
            searchview.setIconifiedByDefault(false);
            searchview.setQuery(query, true);
        }
        /*
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchview.setIconifiedByDefault(false);
            searchview.setQuery(text.get(0), true);
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            // if the intentResult is null then
            // toast a message as "cancelled"
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    String a = (intentResult.getContents());
                    String b = (intentResult.getFormatName());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }*/
    }


    @Override
    public void onBackPressed()
    {
        logout();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        LoadProducts();
    }
}