package no.hiof.informatikk.gruppe6.rusletur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DrawerLayout drawerLayout;

    EditText testText;
    Button testButton;


    EditText userEmail;
    Button signOut;

    /*
    REMOVE ME!!!
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize drawerlayout
        drawerLayout = findViewById(R.id.drawerLayout);

        //Clickhandling on navigationdrawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Make a cool spinning animation for the hamburgermeny when navigationdrawer opens
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //When activity starts, open the fragment immediately. SavedInstanceState handling for rotating phone.
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
        }

        /*
        userEmail = findViewById(R.id.mainScreen_emialField_editText);
        signOut = findViewById(R.id.mainScreen_logout_Button);
        //TODO Remove Activity and class when done testing
        mAuth = FirebaseAuth.getInstance();
        //Get user currently signed in
        mUser = mAuth.getCurrentUser();
        //userEmail.setText(mUser.getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out user from app
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainScreen.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        /*
        *Handle clicking for each item on navigation drawer. Will change this so each item clicked
        * opens a new fragment, which will display the relevant pages of the app once the
        * xml files follow the material design standard
         */
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_trip:
                Toast.makeText(this, "DE E TURAN SOM TÆLLE", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        /*
        * When the navigation drawer is open, clicking back will close the navigation drawer before
        * going to the previous view. Quality of life change, OH YAAAAH
         */

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToMaps(View view) {
        startActivity(new Intent(MainScreen.this, MapsActivity.class));
    }

    public void sendToDebugUserManagement(View view) {
        startActivity(new Intent(this, UserManagmentDebug.class));
    }
    public void goToUserRegistration(View view) {
        startActivity(new Intent(this, UserRegistration.class));
    }

    public void testUIDRetrival(View view) {
        /*
        Method for getting userIDTokens. IDTokens must be used to reference backend, not UID.

         */
        /*
        mUser = mAuth.getInstance().getCurrentUser();

        //testButton = findViewById(R.id.testUIDButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.getUid();
                mUser.getEmail();
                testText.setText(mUser.getDisplayName());

                //Get the current users ID.
                mUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if(task.isSuccessful()){

                                }
                                else {
                                    //Handle error
                                }
                            }
                        });
            }
        });


        */

    }

    public void openRegistrerView(View view) {
        Intent intent = new Intent(MainScreen.this, UserManagement.class);
        startActivity(intent);
    }
    /*
    REMOVE ME!!!
     */
}
