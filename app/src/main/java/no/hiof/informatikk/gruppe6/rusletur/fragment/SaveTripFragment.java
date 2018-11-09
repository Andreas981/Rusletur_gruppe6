package no.hiof.informatikk.gruppe6.rusletur.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import no.hiof.informatikk.gruppe6.rusletur.MainScreen;
import no.hiof.informatikk.gruppe6.rusletur.Model.Fylke;
import no.hiof.informatikk.gruppe6.rusletur.Model.FylkeList;
import no.hiof.informatikk.gruppe6.rusletur.Model.Kommune;
import no.hiof.informatikk.gruppe6.rusletur.R;

import static no.hiof.informatikk.gruppe6.rusletur.fragment.MainMenuFragment.TAG;

public class SaveTripFragment extends Fragment{
    private String selectedDifficulty;
    private EditText nameInput;
    private EditText descInput;
    private RadioGroup difficultyRadioGroup;
    private Button saveTripButton;
    private boolean checked;
    private String nameinput;
    private String description;
    private final View view = null;

    private ArrayList<ArrayList<String>> fylkerOgKommuner = new ArrayList<>();
    private ArrayList<String> tmpFylker;
    private ArrayList<String> tmpKommuner;
    private HttpURLConnection conn = null;

    private Spinner municipalitySpinner;
    private Spinner countySpinner;

    private ArrayAdapter<String> countyAdapter;
    private ArrayAdapter<Kommune> municipalityAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_savetrip, container, false);


        /*
        * This is a pretty simple XML which just displays a name input, description input
        * and RadioButtons for selecting difficulty.
         */

        setupArray();

        nameInput = view.findViewById(R.id.savetrip_nameOfTripInput);
        descInput = view.findViewById(R.id.savetrip_descriptionInput);

        difficultyRadioGroup = (RadioGroup) view.findViewById(R.id.savetrip_radioGroup);

        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.savetrip_easySelected:
                        selectedDifficulty = "Lett";
                        break;
                    case R.id.savetrip_mediumSelected:
                        selectedDifficulty = "Middels";
                        break;
                    case R.id.savetrip_hardSelected:
                        selectedDifficulty = "Vanskelig";
                        break;
                }
            }
        });



        saveTripButton = view.findViewById(R.id.savetrip_saveTripButton);
        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput != null && descInput != null && selectedDifficulty != null) {
                    /*
                    * Check if input is not null lol.
                    * If everything checks out, send input from name, description and radiogroup to
                    * handleStorageOfTrips in MainScreen, and switch from this fragment back to the
                    * MainMenuFragment. It happens lightning quick because Fragments are fucking rad bro.
                     */
                    nameinput = nameInput.getText().toString();
                    description = descInput.getText().toString();

                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lagring")
                            .setMessage("Vil du dele turen?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainScreen) getActivity()).handleStorageOfTrips(nameinput, description, selectedDifficulty);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();

                                }
                            })
                            .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainScreen) getActivity()).handleOfflineStorageOfTrips(nameinput, description, selectedDifficulty);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(getActivity(), "Er alle feltene fylt ut?", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void setUpCountySpinner(){

        Log.d(TAG, "setUpCountySpinner: setupSpinner: setup");
        tmpFylker.clear();
        tmpKommuner.clear();
        for(int i = 0; i < fylkerOgKommuner.size(); i++){
            tmpFylker.add(fylkerOgKommuner.get(i).get(0));
        }

        countySpinner = (Spinner)view.findViewById(R.id.savetrip_selectCounty);
        countyAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, tmpFylker);
        countyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        countySpinner.setAdapter(countyAdapter);
    }

    private void setUpMunicipalitySpinner(){
        municipalitySpinner = (Spinner)view.findViewById(R.id.savetrip_selectMunicipality);
        municipalityAdapter = new ArrayAdapter<Kommune>(getActivity(),android.R.layout.simple_list_item_1,FylkeList.getRegisterForFylke().get(0).getKommuneArrayList());
        municipalityAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        municipalitySpinner.setAdapter(municipalityAdapter);
    }

    private void setupArray(){
        Log.d(TAG, "setupArray: setupSpinner: setup array");
        try {
            URL url =  new URL("https://raw.githubusercontent.com/Andreni/Rusletur_gruppe6/master/fylkerMedKommuner.txt?token=Ae4q33Lvwx1AhhoKJi7-i8pPs-nqFcIQks5b7uiJwA%3D%3D");

            conn = (HttpURLConnection) url.openConnection();

            InputStream in = conn.getInputStream();
            if(conn.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while((inputLine = br.readLine()) != null){
                    String[] fylkeKommune = inputLine.split("|");
                    boolean fylkeExists = false;
                    int fylkeIndex = 0;
                    for(int i = 0; i < fylkerOgKommuner.size(); i++){
                        if (fylkerOgKommuner.get(i).get(0) == fylkeKommune[0]) {
                            fylkeExists = true;
                            fylkeIndex = i;
                            break;
                        }
                    }
                    if(fylkeExists){
                        fylkerOgKommuner.get(fylkeIndex).add(fylkeKommune[1]);
                    }else{
                        fylkerOgKommuner.get(fylkerOgKommuner.size()-1).add(fylkeKommune[0]);
                        fylkerOgKommuner.get(fylkerOgKommuner.size()-1).add(fylkeKommune[1]);
                    }
                }
            }

            setUpCountySpinner();

        }catch (MalformedURLException e){
            Log.d(TAG, "setupArray: setupSpinner: MAlformed url exception");
            e.printStackTrace();
        }catch (IOException e){
            Log.d(TAG, "setupArray: setupSpinner: IOexception");
            e.printStackTrace();
        }



    }


}


