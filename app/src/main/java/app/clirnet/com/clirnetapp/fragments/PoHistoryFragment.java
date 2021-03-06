package app.clirnet.com.clirnetapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import app.clirnet.com.clirnetapp.R;
import app.clirnet.com.clirnetapp.activity.NavigationActivity;
import app.clirnet.com.clirnetapp.activity.PrivacyPolicy;
import app.clirnet.com.clirnetapp.activity.ShowPersonalDetailsActivity;
import app.clirnet.com.clirnetapp.activity.TermsCondition;
import app.clirnet.com.clirnetapp.adapters.PoHistoryAdapter;
import app.clirnet.com.clirnetapp.app.AppController;
import app.clirnet.com.clirnetapp.helper.BannerClass;
import app.clirnet.com.clirnetapp.helper.DatabaseClass;
import app.clirnet.com.clirnetapp.helper.LastnameDatabaseClass;
import app.clirnet.com.clirnetapp.helper.SQLController;
import app.clirnet.com.clirnetapp.helper.SQLiteHandler;
import app.clirnet.com.clirnetapp.models.Counts;
import app.clirnet.com.clirnetapp.models.RegistrationModel;
import app.clirnet.com.clirnetapp.utility.CSVWriter;
import app.clirnet.com.clirnetapp.utility.ItemClickListener;
import app.clirnet.com.clirnetapp.utility.MultiSpinner;
import app.clirnet.com.clirnetapp.utility.MultiSpinner2;


public class PoHistoryFragment extends Fragment implements MultiSpinner.MultiSpinnerListener, MultiSpinner2.MultiSpinnerListener {


    private OnFragmentInteractionListener mListener;
    private static Bundle healthDataBundle;
    private EditText firstName;
    private AutoCompleteTextView lastName;
    private EditText phone_no;
    private String strfname;
    private String strlname;
    private String strpno;

    private SQLController sqlController;
    private AppController appController;
    private BannerClass bannerClass;

    private PoHistoryAdapter poHistoryAdapter;
    private RecyclerView recyclerView;
    private ImageView backChangingImages;
    private ArrayList<RegistrationModel> patientData = new ArrayList<>();
    private LinearLayout norecordtv;
    private View rootview;

    private Button submit;
    private MultiSpinner genderSpinner;

    private int ival = 0;
    private int loadLimit = 25;

    private int[] selectedItems = {0, 0, 0, 0};
    private int[] selectedItems2 = {0, 0, 0, 0, 0, 0, 0, 0};
    private ArrayList genderList;

    private ArrayList selectedListGender;
    private ArrayList selectedAgeList;
    private MultiSpinner2 ageGapSpinner;
    private ArrayList ageList;
    private MultiAutoCompleteTextView symptomsDiagnosis;
    private DatabaseClass databaseClass;
    private LastnameDatabaseClass lastnameDatabaseClass;
    private ArrayList<String> mAilmemtArrayList;
    private ArrayList selectedAilmentList;
    private LinearLayoutManager mLayoutManager;

    private final int PAGE_SIZE = 2;

    private boolean isLoading = false;

    private int queryCount;
    private ArrayList<String> bannerimgNames;

    private String doctor_membership_number;
    private Button resetFilters;
    private Integer weightMinValue, weightMaxValue;
    private Integer heightMinValue, heightMaxValue;
    private Integer bmiMinValue, bmiMaxValue;
    private Integer pulseMinValue, pulseMaxValue;
    private Integer tempMinValue, tempMaxValue;
    private Integer systoleMinValue, systoleMaxValue;
    private Integer distoleMinValue, distoleMaxValue;
    private Integer sugarFpgMinValue, sugarFpgMaxValue;
    private Integer sugarPpgMinValue, sugarPpgMaxValue;
    private Integer spo2MinValue, spo2MaxValue;
    private Integer respirationMinValue, respirationMaxValue;
    private Integer strMinWeight;
    private Integer strMaxWeight;
    private Integer strMinHeight;
    private Integer strMaxHeight;
    private Integer strMinBmi;
    private Integer strMaxBmi;
    private Integer strMinPulse;
    private Integer strMaxPulse;
    private Integer strMinTemp;
    private Integer strMaxTemp;
    private Integer strMinSystole;
    private Integer strMaxSystole;
    private Integer strMinDistole;
    private Integer strMaxDistole;
    private Integer strMinSugarFPG;
    private Integer strMaxSugarFPG;
    private Integer strMinSugarPPG;
    private Integer strMaxSugarPPG;
    private Integer strMinSpo2;
    private Integer strMaxSpo2;
    private Integer strMinRespiration;
    private Integer strMaxRespiration;
    private EditText input_min_weight;
    private EditText input_max_weight;
    private EditText input_min_height;
    private EditText input_max_height;
    private EditText input_min_bmi;
    private EditText input_max_bmi;
    private EditText input_min_pulse;
    private EditText input_max_pulse;
    private EditText input_min_temp;
    private EditText input_max_temp;
    private EditText input_min_systole;
    private EditText input_max_systole;
    private EditText input_min_distole;
    private EditText input_max_distole;
    private EditText input_min_sugarfpg;
    private EditText input_max_sugarfpg;
    private EditText input_min_sugarppg;
    private EditText input_max_sugarppg;
    private EditText input_min_sPo2, input_max_sPo2;
    private EditText input_min_respiration;
    private EditText input_max_respiration;


    private StringBuilder sb = new StringBuilder();
    private String strEcg;
    private String strPft;
    private String strLipidTC;
    private String strLipidTG;
    private String strLipidLDL;
    private String strLipidVHDL;
    private String strLipidHDL;
    private Integer strHbA1c;
    private String strSerumUrea;
    private String strAcer;
    private String strAllergie;

    private String strLifeStyle;
    private String strStressLevel;
    private String strExcercise;
    private String strBingeEating;
    private String strSexuallyActive;
    private String strFoodHabit;
    private String strFoodPreference;
    private String strSmoking;
    private String strTobaco;
    private String strAlcoholConsumption;
    private String noOfSticksPerYear;
    private String noOfPegsPerYear;
    private Button filterVitals;
    private String strLeftSmokingSinceYear;
    private String strLeftAlcoholSinceYear;
    private String otherTobacoTaking;
    private String strDrug;
    private String otherDrugTaking;
    private String strLactoseTolerance;
    private String strSleepStatus;
    private String strLipidTCMax;
    private String strLipidTGMax;
    private String strLipidLDLMax;
    private String strLipidVHDLMax;
    private String strLipidHDLMax;
    private Integer strHbA1cMax;
    private String strSerumUreaMax;
    private String strAcerMax;

    private String strPallore;
    private String strCyanosis, strTremors, strIcterus, strClubbing, strOedema, strCalfTenderness, strLymphadenopathy;

    private String strPallorDescription;
    private String strCyanosisDescription;
    private String strTremorsDescription;
    private String strIcterusDescription;
    private String strClubbingDescription;
    private String strOedemaDescription;
    private String strCalfTendernessDescription;
    private String strLymphadenopathyDescription;

    private Button filterObservation;
    private Button filterInvestigation;
    private Integer mMinHbA1c, mMaxHbA1c;
    private Integer mMinAcer, mMaxAcer;
    private Integer mMinSerumUrea, mMaxSerumUrea;
    private Integer mMinTc, mMaxTc;
    private Integer mMinTg, mMaxTg;
    private Integer mMinHdl, mMaxHdl;
    private Integer mMinLdl, mMaxLdl;
    private Integer mMinVhdl, mMaxVhdl;
    private AutoLabelUI mAutoLabel;
    private AutoLabelUI mInvestigationLabel;
    private Button filterHealth;
    private String valMinSpo2;
    private String valMaxSpo2;
    private LinearLayout tabSubmitWarnig;
    private String exportChoice;


    public PoHistoryFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        rootview = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = inflater.inflate(R.layout.fragment_po_history, container, false);

        ((NavigationActivity) getActivity()).setActionBarTitle("Patient History");

        firstName = (EditText) rootview.findViewById(R.id.firstname);

        lastName = (AutoCompleteTextView) rootview.findViewById(R.id.lastname);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        symptomsDiagnosis = (MultiAutoCompleteTextView) rootview.findViewById(R.id.symptoms);
        TextView currdate = (TextView) rootview.findViewById(R.id.sysdate);
        backChangingImages = (ImageView) rootview.findViewById(R.id.backChangingImages);
        norecordtv = (LinearLayout) rootview.findViewById(R.id.norecordtv);

        genderSpinner = (MultiSpinner) rootview.findViewById(R.id.gender);
        ageGapSpinner = (MultiSpinner2) rootview.findViewById(R.id.ageGap);


        phone_no = (EditText) rootview.findViewById(R.id.mobile_no);

        filterInvestigation = (Button) rootview.findViewById(R.id.filterInvestigation);
        filterHealth = (Button) rootview.findViewById(R.id.filterHealth);
        filterVitals = (Button) rootview.findViewById(R.id.filterVitals);
        filterObservation = (Button) rootview.findViewById(R.id.filterObservation);
        final Button exportData = (Button) rootview.findViewById(R.id.export_data);

        tabSubmitWarnig = (LinearLayout) rootview.findViewById(R.id.tabSubmitWarnig);


        mAutoLabel = (AutoLabelUI) rootview.findViewById(R.id.label_view);
        mInvestigationLabel = (AutoLabelUI) rootview.findViewById(R.id.investigation_label_view);
        setListeners();
        setAutoLabelUISettings();

        //initalizeView(rootview);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM , yyyy", Locale.ENGLISH);
        Date todayDate = new Date();
        String dd = sdf.format(todayDate);


        currdate.setText("Today's Date : " + dd);

        TextView privacyPolicy = (TextView) rootview.findViewById(R.id.privacyPolicy);
        TextView termsAndCondition = (TextView) rootview.findViewById(R.id.termsandCondition);

        //open privacy policy page
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent);

            }
        });

        //open Terms and Condition page
        termsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), TermsCondition.class);
                startActivity(intent);

            }
        });

        try {
            sqlController = new SQLController(getContext().getApplicationContext());
            sqlController.open();
            if (databaseClass == null) {
                databaseClass = new DatabaseClass(getContext());
            }
            if (bannerClass == null) {
                bannerClass = new BannerClass(getContext());
            }
            if (appController == null) {
                appController = new AppController();
            }
            if (lastnameDatabaseClass == null) {
                lastnameDatabaseClass = new LastnameDatabaseClass(getContext());
            }
            bannerimgNames = bannerClass.getImageName();
            doctor_membership_number = sqlController.getDoctorMembershipIdNew();

            ArrayList<HashMap<String, Integer>> vitalsMinMaxList = sqlController.getMinMaxVitals();
            ArrayList<HashMap<String, Integer>> investigationMinMaxList = sqlController.getMinMaxInvestigation();

            if (vitalsMinMaxList.size() > 0) {

                strMinWeight = vitalsMinMaxList.get(0).get("MINWEIGHT");
                strMaxWeight = vitalsMinMaxList.get(0).get("MAXWEIGHT");

                strMinHeight = vitalsMinMaxList.get(0).get("MINHEIGHT");
                strMaxHeight = vitalsMinMaxList.get(0).get("MAXHEIGHT");
                strMinBmi = vitalsMinMaxList.get(0).get("MINBMI");
                strMaxBmi = vitalsMinMaxList.get(0).get("MAXBMI");
                strMinPulse = vitalsMinMaxList.get(0).get("MINPULSE");
                strMaxPulse = vitalsMinMaxList.get(0).get("MAXPULSE");
                strMinTemp = vitalsMinMaxList.get(0).get("MINTEMP");
                strMaxTemp = vitalsMinMaxList.get(0).get("MAXTEMP");
                strMinSystole = vitalsMinMaxList.get(0).get("MINSYSTOLE");
                strMaxSystole = vitalsMinMaxList.get(0).get("MAXSYSTOLE");
                strMinDistole = vitalsMinMaxList.get(0).get("MINDISTOLE");
                strMaxDistole = vitalsMinMaxList.get(0).get("MAXDISTOLE");
                strMinSpo2 = vitalsMinMaxList.get(0).get("MINSPO2");
                strMaxSpo2 = vitalsMinMaxList.get(0).get("MAXSPO2");
                strMinRespiration = vitalsMinMaxList.get(0).get("MINRESPIRATION");
                strMaxRespiration = vitalsMinMaxList.get(0).get("MAXRESPIRATION");
                strMinSugarFPG = vitalsMinMaxList.get(0).get("MINSUGARFPG");
                strMaxSugarFPG = vitalsMinMaxList.get(0).get("MAXSUGARFPG");
                strMinSugarPPG = vitalsMinMaxList.get(0).get("MINSUGARPPG");
                strMaxSugarPPG = vitalsMinMaxList.get(0).get("MAXSUGARPPG");
            }
            if (investigationMinMaxList.size() > 0) {

                mMinHbA1c = investigationMinMaxList.get(0).get("MINHbA1c");
                mMaxHbA1c = investigationMinMaxList.get(0).get("MAXHbA1c");
                mMinAcer = investigationMinMaxList.get(0).get("MINAcer");
                mMaxAcer = investigationMinMaxList.get(0).get("MAXAcer");
                mMinSerumUrea = investigationMinMaxList.get(0).get("MINSerumUrea");
                mMaxSerumUrea = investigationMinMaxList.get(0).get("MAXSerumUrea");
                mMinTc = investigationMinMaxList.get(0).get("MINTC");
                mMaxTc = investigationMinMaxList.get(0).get("MAXTC");
                mMinTg = investigationMinMaxList.get(0).get("MINTG");
                mMaxTg = investigationMinMaxList.get(0).get("MAXTG");
                mMinHdl = investigationMinMaxList.get(0).get("MINHDL");
                mMaxHdl = investigationMinMaxList.get(0).get("MAXHDL");
                mMinLdl = investigationMinMaxList.get(0).get("MINLDL");
                mMaxLdl = investigationMinMaxList.get(0).get("MAXLDL");
                mMinVhdl = investigationMinMaxList.get(0).get("MINVHDL");
                mMaxVhdl = investigationMinMaxList.get(0).get("MAXVHDL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            appController.appendLog(appController.getDateTime() + " " + "/ " + "Po History Frgament" + e + " " + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }

        patientData.clear(); //This method will clear all previous data from  Array list  24-8-2016

        setAilmentData();

        submit = (Button) rootview.findViewById(R.id.submit);

        submit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    tabSubmitWarnig.setVisibility(View.GONE);
                    /*exportData.setVisibility(View.VISIBLE);*/

                    submit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));
                    patientData.clear(); //This method will clear all previous data from  Array list  24-8-2016
                    if (poHistoryAdapter != null) {
                        poHistoryAdapter.notifyDataSetChanged();
                    }
                    //data from health dialog fragment
                    if (healthDataBundle != null) {

                        strSmoking = healthDataBundle.getString("SMOKING");
                        noOfSticksPerYear = healthDataBundle.getString("NUMODSTICKS");
                        strLeftSmokingSinceYear = healthDataBundle.getString("SMOKELEFTSINCE");

                        strAlcoholConsumption = healthDataBundle.getString("DRINKING");
                        noOfPegsPerYear = healthDataBundle.getString("NUMOFPEGS");
                        strLeftAlcoholSinceYear = healthDataBundle.getString("DRINKINGLEFTSINCE");
                        strTobaco = healthDataBundle.getString("TOBACO");
                        otherTobacoTaking = healthDataBundle.getString("OTHERTOBACO");
                        strDrug = healthDataBundle.getString("DRUG");
                        otherDrugTaking = healthDataBundle.getString("OTHERDRUG");
                        strFoodHabit = healthDataBundle.getString("FOODHABIT");
                        strFoodPreference = healthDataBundle.getString("FOODPREFERNCE");
                        strBingeEating = healthDataBundle.getString("BINFEEATING");
                        strLactoseTolerance = healthDataBundle.getString("LACTOSETOLERNCE");
                        strLifeStyle = healthDataBundle.getString("LIFESTYLE");
                        strSleepStatus = healthDataBundle.getString("SLEEP");
                        strStressLevel = healthDataBundle.getString("STRESSLEVEL");
                        strSexuallyActive = healthDataBundle.getString("SEXUALACTIVITY");
                        strExcercise = healthDataBundle.getString("EXCERCISE");
                        strAllergie = healthDataBundle.getString("ALLERGIES");

                    }
                    strfname = firstName.getText().toString().trim();
                    strlname = lastName.getText().toString().trim();
                    strpno = phone_no.getText().toString().trim();
                    String strAilment = symptomsDiagnosis.getText().toString().trim();

                    //remove comma occurance from string
                    strAilment = appController.removeCommaOccurance(strAilment);
                    //Remove spaces between text if more than 2 white spaces found 12-12-2016
                    strAilment = strAilment.replaceAll("\\s+", " ");

                    String delimiter = ",";
                    String[] temp = strAilment.split(delimiter);
                    selectedAilmentList = new ArrayList();
                           /* print substrings */
                    Collections.addAll(selectedAilmentList, temp);

                    try {
                        ival = 0;
                        loadLimit = 25;
                        patientData = sqlController.getFilterDatanew(strfname, strlname, strpno, selectedListGender, selectedAgeList, selectedAilmentList, ival, loadLimit, weightMinValue, weightMaxValue, heightMinValue, heightMaxValue, bmiMinValue, bmiMaxValue,
                                pulseMinValue, pulseMaxValue, tempMinValue, tempMaxValue, systoleMinValue, systoleMaxValue, distoleMinValue, distoleMaxValue, sugarFpgMinValue, sugarFpgMaxValue, sugarPpgMinValue, sugarPpgMaxValue, strLipidTC, strLipidTCMax, strLipidTG, strLipidTGMax, strLipidLDL, strLipidLDLMax, strLipidVHDL, strLipidVHDLMax, strLipidHDL, strLipidHDLMax, strHbA1c, strHbA1cMax, strSerumUrea, strSerumUreaMax, strAcer, strAcerMax, strEcg, strPft
                                , strSmoking, noOfSticksPerYear, strLeftSmokingSinceYear, strAlcoholConsumption, noOfPegsPerYear, strLeftAlcoholSinceYear, strTobaco, otherTobacoTaking, strDrug, otherDrugTaking, strFoodHabit, strFoodPreference, strBingeEating, strLactoseTolerance, strLifeStyle, strSleepStatus, strStressLevel, strSexuallyActive, strExcercise, strAllergie, strPallore, strPallorDescription, strCyanosis, strCyanosisDescription, strTremors, strTremorsDescription, strIcterus, strIcterusDescription
                                , strClubbing, strClubbingDescription, strOedema, strOedemaDescription, strCalfTenderness, strCalfTendernessDescription, strLymphadenopathy, strLymphadenopathyDescription, spo2MinValue, spo2MaxValue, respirationMinValue, respirationMinValue);

                        queryCount = sqlController.getCountResult();


                        if (patientData.size() > 0) {
                            removeDuplicate(patientData);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        appController.appendLog(appController.getDateTime() + " " + "/ " + "Po History Fragment" + e + " " + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    } finally {
                        if (sqlController != null) {
                            sqlController.close();
                        }
                    }

                    int count = patientData.size();
                    try {
                        if (count > 0) {

                            recyclerView.setVisibility(View.VISIBLE);
                            norecordtv.setVisibility(View.GONE);


                            poHistoryAdapter = new PoHistoryAdapter(patientData, queryCount);

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(poHistoryAdapter);
                            recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

                        } else {
                            recyclerView.setVisibility(View.INVISIBLE);
                            norecordtv.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        appController.appendLog(appController.getDateTime() + " " + "/ " + "Po History Fragment" + e + " " + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    }

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    submit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });

        exportData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                exportDataDialog(0);

               /* try {

                    exportDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });

        recyclerView.addOnItemTouchListener(new HomeFragment.RecyclerTouchListener(getContext(), recyclerView, new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                //redirect to ShowPersonalDetailsActivity 02-11-2016
                recyclerViewOnClick(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //commneted for this build 13-10-2017 ashish this will go on next build

              //  exportDataDialog(position);


               /* final int posi = position;

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Do you want to export patient data?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "All",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    exportDB();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "Single",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                exportDB(posi);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                //exportDB(position);*/

            }

        }));

        setUpSpinner();
        setupAnimation();
        //setValueToSeekBarFromDb();

        filterVitals.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {


                    showVitalsDialogBox();


                    filterVitals.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    filterVitals.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });
        filterObservation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    filterObservation.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));
                    showObservationsDialog();

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    filterObservation.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });

        resetFilters = (Button) rootview.findViewById(R.id.resetFilters);

        resetFilters.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ageGapSpinner.setSelection(0);
                    genderSpinner.setSelection(0);
                    selectedListGender.clear();
                    selectedAgeList.clear();
                    firstName.setText("");
                    lastName.setText("");
                    phone_no.setText("");
                    symptomsDiagnosis.setText("");
                    setUpSpinner();  //reseting spinner value
                    reseFiltersData();
                    exportData.setVisibility(View.INVISIBLE);

                    resetFilters.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    resetFilters.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });
        filterInvestigation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    openInvestigationDialog();

                    filterInvestigation.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    filterInvestigation.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });
        filterHealth.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    filterHealth.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    HealthVitalsDialogFragment testDialog = new HealthVitalsDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBundle("FILTERDATA", healthDataBundle);
                    testDialog.setRetainInstance(true);
                    testDialog.setArguments(bundle);

                    testDialog.show(fm, "fragment_name");

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    filterHealth.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });


        return rootview;
    }

    private void exportDB(int position,String exportChoice) {

        RegistrationModel book = patientData.get(position);
        String patId = book.getPat_id();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        ArrayList<RegistrationModel> list = new ArrayList<>();
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "SelectedPatientHistory.csv");
        try {

            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = new SQLiteHandler(getContext()).getReadableDatabase();

            String selectQuery = "SELECT  p.patient_id,p.first_name, p.middle_name, p.last_name,p.dob,p.age,p.phonenumber,p.gender,p.language,p.photo,ph.follow_up_date, ph.days,ph.weeks,ph.months, ph.ailment,ph.prescription,ph.clinical_notes,p.added_on,ph.visit_date,p.modified_on,ph.key_visit_id,ph.actual_follow_up_date, p.patient_address,p.patient_city_town,p.district,p.pin_code,p.patient_state,ph.weight,ph.pulse,ph.bp_high,ph.bp_low,ph.temperature,ti.sugar,ph.symptoms, ph.diagnosis,p.email," +
                    "p.uid,p.alternate_no,ph.height,ph.bmi,ti.sugar_fasting,p.alternate_phone_type,p.phone_type,p.isd_code,p.alternate_no_isd ,ph.refered_by,ph.refered_to,ph.spo2,ph.respiration,ti.hba1c,ti.acer,ti.serem_urea,ti.lipid_profile_hdl,ti.lipid_profile_tc,lipid_profile_tg,ti.lipid_profile_ldl,ti.lipid_profile_vhdl,ti.ecg,ti.pft,obs.pallor,obs.cyanosis,obs.tremors,obs.icterus,obs.clubbing,obs.oedema,obs.calf_tenderness,obs.lympadenopathy,ph.obesity from  patient p  LEFT JOIN patient_history ph ON p.patient_id = ph.patient_id LEFT JOIN table_investigation ti ON ph.key_visit_id = ti.key_visit_id LEFT JOIN observation obs ON obs.key_visit_id = ph.key_visit_id where p.patient_id=" + patId + "  order by ph.key_visit_id desc ";


            Cursor curCSV = db.rawQuery(selectQuery, null);


            csvWrite.writeNext(curCSV.getColumnNames());

            if (curCSV.moveToFirst()) {
                do {
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(30)};
                    csvWrite.writeNext(arrStr);
                    //Log.e("data",""+curCSV.getString(3)+" "+curCSV.getString(4)+" "+ curCSV.getString(6));

                    RegistrationModel user = new RegistrationModel(curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(30));

                    list.add(user);
                } while (curCSV.moveToNext());
            }



           /* while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13), curCSV.getString(14), curCSV.getString(15), curCSV.getString(16), curCSV.getString(17), curCSV.getString(18), curCSV.getString(19), curCSV.getString(20), curCSV.getString(21), curCSV.getString(22), curCSV.getString(23), curCSV.getString(24), curCSV.getString(25), curCSV.getString(26), curCSV.getString(27), curCSV.getString(28), curCSV.getString(29), curCSV.getString(30), curCSV.getString(31), curCSV.getString(32), curCSV.getString(33), curCSV.getString(34), curCSV.getString(35), curCSV.getString(36), curCSV.getString(37), curCSV.getString(38), curCSV.getString(39), curCSV.getString(40), curCSV.getString(41), curCSV.getString(42), curCSV.getString(43), curCSV.getString(44), curCSV.getString(45), curCSV.getString(46), curCSV.getString(47), curCSV.getString(48), curCSV.getString(49), curCSV.getString(50), curCSV.getString(51), curCSV.getString(52), curCSV.getString(53), curCSV.getString(54), curCSV.getString(55), curCSV.getString(56), curCSV.getString(57), curCSV.getString(58), curCSV.getString(59), curCSV.getString(60), curCSV.getString(61), curCSV.getString(62), curCSV.getString(63), curCSV.getString(64), curCSV.getString(65), curCSV.getString(66)};
                csvWrite.writeNext(arrStr);
            }*/
            csvWrite.close();
            curCSV.close();

            File pdfFile = createPDF(list);  //get the pdf file path 17-10-2017

            Intent emialIntent = new Intent(Intent.ACTION_SEND);
            //  emialIntent.setType("*/*");
            emialIntent.setType("message/rfc822");

            String mailId = "ashish.umredkar@clirnet.com";
            emialIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailId});
            Random r = new Random();
            emialIntent.putExtra(Intent.EXTRA_SUBJECT, "Local Db" + r.nextInt());
            emialIntent.putExtra(Intent.EXTRA_SUBJECT, "Local Database  Name " + "doctor name " + "  Membership Id:  " + doctor_membership_number);
            if(exportChoice.equals("PDF")) {
                emialIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));//actual data
            }else{
                emialIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//actual data
            }
            //emialIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//actual data
            startActivity(Intent.createChooser(emialIntent, "Choose an Email client :"));
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    private void exportDB(String exportChoice) throws IOException {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        CSVWriter csvWrite = null;
        Cursor curCSV = null;
        ArrayList<RegistrationModel> list = new ArrayList<>();

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "AllPatientSearchList.csv");
        try {

            file.createNewFile();
            csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = new SQLiteHandler(getContext()).getReadableDatabase();
            String selectQuery = new Counts().getSelectQuery();

            curCSV = db.rawQuery(selectQuery, null);

            csvWrite.writeNext(curCSV.getColumnNames());
            if (curCSV.moveToFirst()) {
                do {
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13), curCSV.getString(14), curCSV.getString(15), curCSV.getString(16), curCSV.getString(17), curCSV.getString(18), curCSV.getString(19), curCSV.getString(20), curCSV.getString(21), curCSV.getString(22), curCSV.getString(23), curCSV.getString(24), curCSV.getString(25), curCSV.getString(26), curCSV.getString(27), curCSV.getString(28), curCSV.getString(29), curCSV.getString(30), curCSV.getString(31), curCSV.getString(32), curCSV.getString(33), curCSV.getString(34)};
                    csvWrite.writeNext(arrStr);
                    //Log.e("data",""+curCSV.getString(3)+" "+curCSV.getString(4)+" "+ curCSV.getString(6));

                    RegistrationModel user = new RegistrationModel(curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(30));

                    // RegistrationModel user = new RegistrationModel(curCSV.getString(0), curCSV.getString(1), curCSV.getString(3), curCSV.getString(4), curCSV.getString(6));

                    list.add(user);

                } while (curCSV.moveToNext());
            }

            /*while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13), curCSV.getString(14), curCSV.getString(15), curCSV.getString(16), curCSV.getString(17), curCSV.getString(18), curCSV.getString(19), curCSV.getString(20), curCSV.getString(21), curCSV.getString(22), curCSV.getString(23), curCSV.getString(24), curCSV.getString(25), curCSV.getString(26), curCSV.getString(27), curCSV.getString(28), curCSV.getString(29), curCSV.getString(30), curCSV.getString(31), curCSV.getString(32), curCSV.getString(33), curCSV.getString(34)};
                csvWrite.writeNext(arrStr);

            }*/

            // Log.e("curCSV", "" + curCSV.toString());
            Intent emialIntent = new Intent(Intent.ACTION_SEND);
            //  emialIntent.setType("*/*");
            emialIntent.setType("message/rfc822");

            File pdfFile = createPDF(list);  //get the pdf file path 17-10-2017

            String mailId = "ashish.umredkar@clirnet.com";
            emialIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailId});
            Random r = new Random();
            emialIntent.putExtra(Intent.EXTRA_SUBJECT, "Local Db" + r.nextInt());
            emialIntent.putExtra(Intent.EXTRA_SUBJECT, "Local Database  Name " + "ashish" + "  Membership Id:  " + doctor_membership_number);
            if(exportChoice.equals("PDF")) {
                emialIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));//actual data
            }else{
                emialIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//actual data
            }
            startActivity(Intent.createChooser(emialIntent, "Choose an Email client :"));
           // createPDF(list);
            //  CreatePdf(curCSV,curCSV);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        } finally {
            if (csvWrite != null && curCSV != null) {
                csvWrite.close();
                curCSV.close();
            }
        }
    }

    private void setAilmentData() {

        try {
            databaseClass.openDataBase();
            mAilmemtArrayList = new ArrayList<>();
            ArrayList<String> mSymptomsList = lastnameDatabaseClass.getSymptoms();
            ArrayList<String> mDiagnosisList = lastnameDatabaseClass.getDiagnosis();

            mAilmemtArrayList.addAll(mSymptomsList);
            mAilmemtArrayList.addAll(mDiagnosisList);

            ArrayList<String> mLastNameList = lastnameDatabaseClass.getLastNameNew();

            if (mAilmemtArrayList.size() > 0) {

                //this code is for setting list to auto complete text view  8/6/16
                ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, mAilmemtArrayList);

                adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                symptomsDiagnosis.setThreshold(1);

                symptomsDiagnosis.setAdapter(adp);
                symptomsDiagnosis.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

            }
            if (mLastNameList.size() > 0) {
                ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, mLastNameList);

                adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                lastName.setThreshold(1);

                lastName.setAdapter(adp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            appController.appendLog(appController.getDateTime() + "" + "/" + "Po History Fragment" + e + " " + Thread.currentThread().getStackTrace()[2].getLineNumber());
        } finally {
            if (databaseClass != null) {
                databaseClass.close();
                databaseClass = null;
            }
        }
    }

    private void setUpSpinner() {

        selectedListGender = new ArrayList();
        selectedAgeList = new ArrayList();

        genderList = new ArrayList();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Transgender");

        genderSpinner.setItems(genderList, "Select Gender", this);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                //  selectColoursButton.setText(al.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ageList = new ArrayList();
        ageList.add("0-5");
        ageList.add("5-15");
        ageList.add("15-25");
        ageList.add("25-35");
        ageList.add("35-45");
        ageList.add("45-55");
        ageList.add("55-65");
        ageList.add("65-Above");
        ageGapSpinner.setItems(ageList, "Select Age Group", this);

        ageGapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                //Log.e("", "" + (ageList.get(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
    }

    private void recyclerViewOnClick(int position) {

        RegistrationModel book = patientData.get(position);

        Intent i = new Intent(getContext().getApplicationContext(), ShowPersonalDetailsActivity.class);


        i.putExtra("ID", book.getPat_id());


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);

    }

    /////////////Show Search Bar//////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.other_navigation, menu);

        //  MenuItem item = menu.findItem(R.id.spinner);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        return true;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

        selectedListGender.clear();

        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedItems[i] = 1;
                String selGender = genderList.get(i).toString();

                selectedListGender.add(selGender);

            } else selectedItems[i] = 0;
        }

    }

    @Override
    public void onItemsSelected1(boolean[] selected) {
        selectedAgeList.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedItems2[i] = 1;
                String ageString = ageList.get(i).toString();
                selectedAgeList.add(ageString);

            } else selectedItems2[i] = 0;
        }

    }

    public void updateDisplay(Bundle bundle) {

        healthDataBundle = bundle;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void setupAnimation() {

        try {
            appController.setUpAdd(getContext(), bannerimgNames, backChangingImages, doctor_membership_number, "POHistory Fragment");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeDuplicate(final List<RegistrationModel> al) {

        for (int i = 0; i < al.size() - 1; i++) {

            String element = al.get(i).getPat_id();
            // Log.e("element", "" + element);
            for (int j = i + 1; j < al.size(); j++) {
                if (element.equals(al.get(j).getPat_id())) {

                    al.remove(j);
                    j--;

                }
            }
        }

    }


    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            boolean isLastPage = false;
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {

                    isLoading = true;
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                loadDataForAdapter();
                            }
                        }, 2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    private void loadDataForAdapter() {

        isLoading = false;
        ival = ival + 25;
        //  int beforeFilterCount = 0;

        List<RegistrationModel> memberList = new ArrayList<>();
        int end = 0;
        try {
            if (poHistoryAdapter != null) {
                int index = poHistoryAdapter.getItemCount();
                end = index + PAGE_SIZE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (end <= queryCount) {
            try {
                if (sqlController != null) {

                    memberList = sqlController.getFilterDatanew(strfname, strlname, strpno, selectedListGender, selectedAgeList, selectedAilmentList, ival, loadLimit, weightMinValue, weightMaxValue, heightMinValue, heightMaxValue, bmiMinValue, bmiMaxValue,
                            pulseMinValue, pulseMaxValue, tempMinValue, tempMaxValue, systoleMinValue, systoleMaxValue, distoleMinValue, distoleMaxValue, sugarFpgMinValue, sugarFpgMaxValue, sugarPpgMinValue, sugarPpgMaxValue, strLipidTC, strLipidTCMax, strLipidTG, strLipidTGMax, strLipidLDL, strLipidLDLMax, strLipidVHDL, strLipidVHDLMax, strLipidHDL, strLipidHDLMax, strHbA1c, strHbA1cMax, strSerumUrea, strSerumUreaMax, strAcer, strAcerMax, strEcg, strPft
                            , strSmoking, noOfSticksPerYear, strLeftSmokingSinceYear, strAlcoholConsumption, noOfPegsPerYear, strLeftAlcoholSinceYear, strTobaco, otherTobacoTaking, strDrug, otherDrugTaking, strFoodHabit, strFoodPreference, strBingeEating, strLactoseTolerance, strLifeStyle, strSleepStatus, strStressLevel, strSexuallyActive, strExcercise, strAllergie, strPallore, strPallorDescription, strCyanosis, strCyanosisDescription, strTremors, strTremorsDescription, strIcterus, strIcterusDescription
                            , strClubbing, strClubbingDescription, strOedema, strOedemaDescription, strCalfTenderness, strCalfTendernessDescription, strLymphadenopathy, strLymphadenopathyDescription, spo2MinValue, spo2MaxValue, respirationMinValue, respirationMinValue);
                    //getting count of patient  for filter query
                    queryCount = sqlController.getCountResult();


                    if (memberList.size() > 0) {
                        removeDuplicate(memberList);
                    }
                }
            } catch (Exception ex) {
                //catch all the ones I didn't think of.
                ex.printStackTrace();
            }
            try {

                if (poHistoryAdapter != null) {
                    poHistoryAdapter.addAll(memberList);

                    if (end >= queryCount) {
                        poHistoryAdapter.setLoading(false);

                    }
                }
            } catch (Exception e) {
                appController.appendLog(appController.getDateTime() + " " + "/ " + "PoHistory Fragment " + e + " Line Number: " + Thread.currentThread().getStackTrace()[2].getLineNumber());

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(firstName.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        AppController.getInstance().trackScreenView("Po History Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        rootview = null; // view cleaning up!

        if (sqlController != null) {
            sqlController.close();
            sqlController = null;
        }
        if (appController != null) {
            appController = null;
        }
        if (databaseClass != null) {
            databaseClass = null;
        }
        if (bannerClass != null) {
            bannerClass = null;
        }
        if (poHistoryAdapter != null) {
            poHistoryAdapter = null;
        }
        if (lastnameDatabaseClass != null) {
            lastnameDatabaseClass = null;
        }
        bannerimgNames = null;

        patientData.clear();
        patientData = null;

        recyclerView.setOnClickListener(null);
        norecordtv = null;

        recyclerView = null;
        strfname = null;
        strlname = null;
        strpno = null;
        submit = null;
        selectedListGender = null;
        selectedAgeList = null;
        ageGapSpinner = null;
        ageList = null;
        symptomsDiagnosis = null;
        mAilmemtArrayList = null;

        selectedItems = null;
        selectedItems2 = null;
        genderList = null;
        selectedAilmentList = null;
        mLayoutManager = null;
        doctor_membership_number = null;
        backChangingImages = null;
        firstName = null;
        lastName = null;
        phone_no = null;
        genderSpinner = null;
        healthDataBundle = null;
        filterObservation = null;
        filterObservation = null;
        clearVitalsValue();
        reseFiltersData();

    }

    private void showVitalsDialogBox() {

        final Dialog dialog = new Dialog(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());

        final View f = factory.inflate(R.layout.filter_vitals_dialog_pohistory, null);

        input_min_weight = (EditText) f.findViewById(R.id.input_min_weight);
        input_max_weight = (EditText) f.findViewById(R.id.input_max_weight);
        input_min_height = (EditText) f.findViewById(R.id.input_min_height);
        input_max_height = (EditText) f.findViewById(R.id.input_max_height);
        input_min_bmi = (EditText) f.findViewById(R.id.input_min_bmi);
        input_max_bmi = (EditText) f.findViewById(R.id.input_max_bmi);
        input_min_pulse = (EditText) f.findViewById(R.id.input_min_pulse);
        input_max_pulse = (EditText) f.findViewById(R.id.input_max_pulse);
        input_min_temp = (EditText) f.findViewById(R.id.input_min_temp);
        input_max_temp = (EditText) f.findViewById(R.id.input_max_temp);
        input_min_systole = (EditText) f.findViewById(R.id.input_min_systole);
        input_max_systole = (EditText) f.findViewById(R.id.input_max_systole);
        input_min_distole = (EditText) f.findViewById(R.id.input_min_distole);
        input_max_distole = (EditText) f.findViewById(R.id.input_max_distole);

        input_min_sPo2 = (EditText) f.findViewById(R.id.input_min_spo2);
        input_max_sPo2 = (EditText) f.findViewById(R.id.input_max_spo2);
        input_min_respiration = (EditText) f.findViewById(R.id.input_min_respiration);
        input_max_respiration = (EditText) f.findViewById(R.id.input_max_respiration);
        /* Clear The entered viatls for next search*/

        sb.setLength(0);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(f);

        dialog.setTitle("Filter Vitals");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(f);
        final Button dialogButtonCancel = (Button) f.findViewById(R.id.customDialogCancel);
        final Button dialogButtonOk = (Button) f.findViewById(R.id.customDialogOk);

        //Log.e("weightMinValue", "  " + weightMinValue);


        if (weightMinValue != null && weightMaxValue != null) {
            input_min_weight.setText(weightMinValue.toString());
            input_max_weight.setText(weightMaxValue.toString());

        }
        if (heightMinValue != null && heightMaxValue != null) {
            input_min_height.setText(heightMinValue.toString());
            input_max_height.setText(heightMaxValue.toString());
        }
        if (bmiMinValue != null && bmiMaxValue != null) {
            input_min_bmi.setText(bmiMinValue.toString());
            input_max_bmi.setText(bmiMaxValue.toString());
        }

        if (pulseMinValue != null && pulseMaxValue != null) {
            input_min_pulse.setText(pulseMinValue.toString());
            input_max_pulse.setText(pulseMaxValue.toString());
        }
        if (tempMinValue != null && tempMaxValue != null) {
            input_min_temp.setText(tempMinValue.toString());
            input_max_temp.setText(tempMaxValue.toString());
        }
        if (systoleMinValue != null && systoleMaxValue != null) {
            input_min_systole.setText(systoleMinValue.toString());
            input_max_systole.setText(systoleMaxValue.toString());
        }
        if (distoleMinValue != null && distoleMaxValue != null) {
            input_min_distole.setText(distoleMinValue.toString());
            input_max_distole.setText(distoleMaxValue.toString());
        }
        if (spo2MinValue != null && spo2MaxValue != null) {
            input_min_sPo2.setText(spo2MinValue.toString());
            input_max_sPo2.setText(spo2MaxValue.toString());

        }
        if (respirationMinValue != null && respirationMaxValue != null) {
            input_min_respiration.setText(respirationMinValue.toString());
            input_max_respiration.setText(respirationMaxValue.toString());

        }
        if (sugarFpgMinValue != null && sugarFpgMaxValue != null) {
            input_min_sugarfpg.setText(sugarFpgMinValue.toString());
            input_max_sugarfpg.setText(sugarFpgMaxValue.toString());
        }
        if (sugarPpgMinValue != null && sugarPpgMaxValue != null) {
            input_min_sugarppg.setText(sugarPpgMinValue.toString());
            input_max_sugarppg.setText(sugarPpgMaxValue.toString());
        }

        // Click cancel to dismiss android custom dialog box
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialogButtonCancel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dialogButtonCancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));

                    dialog.dismiss();

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dialogButtonCancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));


                }
                return false;
            }

        });


        dialogButtonOk.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    dialogButtonOk.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dialogButtonOk.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mAutoLabel.clear();

                String valMinWeight = input_min_weight.getText().toString();
                String valMaxWeight = input_max_weight.getText().toString();


                if (valMinWeight != null && !valMinWeight.equals("") && valMaxWeight == null || valMaxWeight.equals("")) {

                    valMaxWeight = String.valueOf(strMaxWeight);
                } else if (valMaxWeight != null && !valMaxWeight.equals("") && valMinWeight == null || valMinWeight.equals("")) {

                    valMinWeight = String.valueOf(strMinWeight);
                } else if (valMinWeight == null && valMaxWeight == null) {
                    valMinWeight = null;
                    valMaxWeight = null;
                }
                if (valMinWeight != null && valMaxWeight != null && !valMinWeight.equals("") && !valMaxWeight.equals("")) {

                    weightMinValue = Integer.valueOf(valMinWeight);
                    weightMaxValue = Integer.valueOf(valMaxWeight);

                    //   sb.append(" Weight: Min ").append(weightMinValue).append(" - Max ").append(weightMaxValue).append(" ;");

                    if (weightMaxValue < weightMinValue) {
                        input_max_weight.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (weightMinValue > strMaxWeight) {
                        input_min_weight.setError(getResources().getString(R.string.mingreatmax) + strMaxWeight);
                        return;
                    } else if (weightMaxValue > strMaxWeight) {
                        input_max_weight.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxWeight);
                        return;
                    }
                }
                if (!valMinWeight.isEmpty() && !valMaxWeight.isEmpty()) {

                    mAutoLabel.addLabel("Weight " + valMinWeight + "-" + valMaxWeight);

                }
                String valMinHeight = input_min_height.getText().toString();
                String valMaxHeight = input_max_height.getText().toString();

                if (valMinHeight != null && valMaxHeight == null || valMaxHeight.equals("")) {
                    valMaxHeight = String.valueOf(strMaxHeight);
                } else if (valMaxWeight != null && valMinHeight == null || valMinHeight.equals("")) {
                    valMinHeight = String.valueOf(strMinHeight);
                } else if (valMinHeight == null && valMaxHeight == null) {
                    valMinHeight = null;
                    valMaxHeight = null;

                }
                if (valMinHeight != null && valMaxHeight != null && !valMinHeight.equals("") && !valMaxHeight.equals("")) {

                    heightMinValue = Integer.valueOf(valMinHeight);
                    heightMaxValue = Integer.valueOf(valMaxHeight);

                    if (heightMaxValue < heightMinValue) {
                        input_max_height.setError(getResources().getString(R.string.minmaxvalid));
                        return;
                    } else if (heightMinValue > heightMaxValue) {
                        input_min_height.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    }
                }
                if (!valMinHeight.isEmpty() && !valMaxHeight.isEmpty()) {

                    mAutoLabel.addLabel("Height " + valMinHeight + "-" + valMaxHeight);
                }

                String valMinBmi = input_min_bmi.getText().toString();
                String valMaxBmi = input_max_bmi.getText().toString();

                if (valMinBmi != null && valMaxBmi == null || valMaxBmi.equals("")) {
                    valMaxBmi = String.valueOf(strMaxBmi);
                } else if (valMaxBmi != null && valMinBmi == null || valMinBmi.equals("")) {
                    valMinBmi = String.valueOf(strMinBmi);
                } else if (valMinBmi == null && valMaxBmi == null) {
                    valMinBmi = null;
                    valMaxBmi = null;
                }

                if (valMinBmi != null && valMaxBmi != null && !valMinBmi.equals("") && !valMaxBmi.equals("")) {

                    bmiMinValue = Integer.valueOf(valMinBmi);
                    bmiMaxValue = Integer.valueOf(valMaxBmi);

                    if (bmiMaxValue < bmiMinValue) {
                        input_max_bmi.setError(getResources().getString(R.string.minmaxvalid));
                        return;
                    } else if (bmiMinValue > bmiMaxValue) {
                        input_min_bmi.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (bmiMaxValue > strMaxBmi) {
                        input_max_bmi.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxBmi);
                        return;
                    }
                }
                if (!valMinBmi.isEmpty() && !valMaxHeight.isEmpty()) {

                    mAutoLabel.addLabel("Bmi " + valMinBmi + "-" + valMaxBmi);
                }
                String valMinPulse = input_min_pulse.getText().toString();
                String valMaxPulse = input_max_pulse.getText().toString();

                if (valMinPulse != null && valMaxPulse == null || valMaxPulse.equals("")) {
                    valMaxPulse = String.valueOf(strMaxPulse);
                } else if (valMaxPulse != null && valMinPulse == null || valMinPulse.equals("")) {
                    valMinPulse = String.valueOf(strMinPulse);
                } else if (valMaxPulse == null && valMinPulse == null) {
                    valMinPulse = null;
                    valMaxPulse = null;
                }

                if (valMinPulse != null && valMaxPulse != null && !valMinPulse.equals("") && !valMaxPulse.equals("")) {

                    pulseMinValue = Integer.valueOf(valMinPulse);
                    pulseMaxValue = Integer.valueOf(valMaxPulse);
                    if (pulseMaxValue < pulseMinValue) {
                        input_min_pulse.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (pulseMinValue > pulseMaxValue) {
                        input_min_pulse.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (pulseMaxValue > strMaxPulse) {
                        input_max_pulse.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxPulse);
                        return;
                    }
                }
                if (!valMinPulse.isEmpty() && !valMinPulse.isEmpty()) {

                    mAutoLabel.addLabel("Pulse " + valMinPulse + "-" + valMaxPulse);
                }
                String valMinTemp = input_min_temp.getText().toString();
                String valMaxTemp = input_max_temp.getText().toString();

                if (valMinTemp != null && valMaxTemp == null || valMaxTemp.equals("")) {
                    valMaxTemp = String.valueOf(strMaxTemp);
                } else if (valMaxTemp != null && valMinTemp == null || valMinTemp.equals("")) {
                    valMinTemp = String.valueOf(strMinTemp);
                } else if (valMinTemp == null && valMaxTemp == null) {
                    valMinTemp = null;
                    valMaxTemp = null;
                }

                if (valMinTemp != null && valMaxTemp != null && !valMinTemp.equals("") && !valMaxTemp.equals("")) {

                    tempMinValue = Integer.valueOf(valMinTemp);
                    tempMaxValue = Integer.valueOf(valMaxTemp);

                    if (tempMaxValue < tempMinValue) {
                        input_max_temp.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (tempMinValue > tempMaxValue) {
                        input_min_temp.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (tempMaxValue > strMaxTemp) {
                        input_max_temp.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxTemp);
                        return;
                    }
                }
                if (!valMinTemp.isEmpty() && !valMaxTemp.isEmpty()) {

                    mAutoLabel.addLabel("Temp " + valMinTemp + "-" + valMaxTemp);
                }

                String valMinSystole = input_min_systole.getText().toString();
                String valMaxSystole = input_max_systole.getText().toString();

                if (valMinSystole != null && valMaxSystole == null || valMaxSystole.equals("")) {
                    valMaxSystole = String.valueOf(strMaxSystole);
                } else if (valMaxSystole != null && valMinSystole == null || valMinSystole.equals("")) {
                    valMinSystole = String.valueOf(strMinSystole);
                } else if (valMinSystole == null && valMaxSystole == null) {
                    valMinSystole = null;
                    valMaxSystole = null;
                }

                if (valMinSystole != null && valMaxSystole != null && !valMinSystole.equals("") && !valMaxSystole.equals("")) {

                    systoleMinValue = Integer.valueOf(valMinSystole);
                    systoleMaxValue = Integer.valueOf(valMaxSystole);


                    if (systoleMaxValue < systoleMinValue) {
                        input_max_systole.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (systoleMinValue > systoleMaxValue) {
                        input_min_systole.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (systoleMaxValue > strMaxSystole) {
                        input_max_systole.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxSystole);
                        return;
                    }
                }
                if (!valMinSystole.isEmpty() && !valMaxSystole.isEmpty()) {

                    mAutoLabel.addLabel("Systole " + valMinSystole + "-" + valMaxSystole);
                }
                String valMinDistole = input_min_distole.getText().toString();
                String valMaxDistole = input_max_distole.getText().toString();

                if (valMinDistole != null && valMaxDistole == null || valMaxDistole.equals("")) {
                    valMaxDistole = String.valueOf(strMaxDistole);
                } else if (valMaxDistole != null && valMinDistole == null || valMinDistole.equals("")) {
                    valMinDistole = String.valueOf(strMinDistole);
                } else if (valMinDistole == null && valMaxDistole == null) {
                    valMinDistole = null;
                    valMaxDistole = null;
                }

                if (valMinDistole != null && valMaxDistole != null && !valMinDistole.equals("") && !valMaxDistole.equals("")) {

                    distoleMinValue = Integer.valueOf(valMinDistole);
                    distoleMaxValue = Integer.valueOf(valMaxDistole);


                    if (distoleMaxValue < distoleMinValue) {
                        input_max_distole.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (distoleMinValue > distoleMaxValue) {
                        input_min_distole.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (distoleMaxValue > strMaxDistole) {
                        input_max_distole.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxDistole);
                        return;
                    }
                }
                if (!valMinDistole.isEmpty() && !valMaxDistole.isEmpty()) {

                    mAutoLabel.addLabel("Diastole " + valMinDistole + "-" + valMaxDistole);
                }

                ///////////////////

                valMinSpo2 = input_min_sPo2.getText().toString();
                valMaxSpo2 = input_max_sPo2.getText().toString();

                if (valMinSpo2 != null && valMaxSpo2 == null || valMaxSpo2.equals("")) {
                    valMaxSpo2 = String.valueOf(strMaxSpo2);
                } else if (valMaxSpo2 != null && valMinSpo2 == null || valMinSpo2.equals("")) {
                    valMinSpo2 = String.valueOf(strMinSpo2);
                } else if (valMinSpo2 == null && valMaxSpo2 == null) {
                    valMinSpo2 = null;
                    valMaxSpo2 = null;
                }

                if (valMinSpo2 != null && valMaxSpo2 != null && !valMinSpo2.equals("") && !valMaxSpo2.equals("")) {

                    spo2MinValue = Integer.valueOf(valMinSpo2);
                    spo2MaxValue = Integer.valueOf(valMaxSpo2);

                    if (spo2MaxValue < spo2MinValue) {
                        input_max_sPo2.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (spo2MinValue > spo2MaxValue) {
                        input_min_sPo2.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (spo2MaxValue > strMaxSpo2) {
                        input_max_sPo2.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxSpo2);
                        return;
                    }
                }
                if (!valMinSpo2.isEmpty() && !valMaxSpo2.isEmpty()) {

                    mAutoLabel.addLabel("Spo2 " + valMinSpo2 + "-" + valMaxSpo2);
                }
                //////////////////////////////////////////////////////////////////////

                String valMinRespiration = input_min_respiration.getText().toString();
                String valMaxRespiration = input_max_respiration.getText().toString();

                if (valMinRespiration != null && valMaxRespiration == null || valMaxRespiration.equals("")) {
                    valMaxRespiration = String.valueOf(strMaxRespiration);
                } else if (valMaxRespiration != null && valMinRespiration == null || valMinRespiration.equals("")) {
                    valMinRespiration = String.valueOf(strMinRespiration);
                } else if (valMinSpo2 == null && valMaxRespiration == null) {
                    valMinRespiration = null;
                    valMaxRespiration = null;
                }

                if (valMinRespiration != null && valMaxRespiration != null && !valMinRespiration.equals("") && !valMaxRespiration.equals("")) {

                    respirationMinValue = Integer.valueOf(valMinRespiration);
                    respirationMaxValue = Integer.valueOf(valMaxRespiration);

                    if (respirationMaxValue < respirationMinValue) {
                        input_max_respiration.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (respirationMinValue > respirationMaxValue) {
                        input_min_respiration.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (respirationMaxValue > strMaxRespiration) {
                        input_max_respiration.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxRespiration);
                        return;
                    }
                }
                if (!valMinRespiration.isEmpty() && !valMaxSpo2.isEmpty()) {

                    mAutoLabel.addLabel("Respiration " + valMinRespiration + "-" + valMaxRespiration);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void clearVitalsValue() {
        weightMinValue = null;
        weightMaxValue = null;
        heightMinValue = null;
        heightMaxValue = null;
        bmiMinValue = null;
        bmiMaxValue = null;
        pulseMinValue = null;
        pulseMaxValue = null;
        tempMinValue = null;
        tempMaxValue = null;
        systoleMinValue = null;
        systoleMaxValue = null;
        distoleMinValue = null;
        distoleMaxValue = null;
        sugarFpgMinValue = null;
        sugarFpgMaxValue = null;
        sugarPpgMinValue = null;
        sugarPpgMaxValue = null;
        strMinWeight = null;
        strMaxWeight = null;
        strMinHeight = null;
        strMaxHeight = null;
        strMinBmi = null;
        strMaxBmi = null;
        strMinPulse = null;
        strMaxPulse = null;
        strMinTemp = null;
        strMaxTemp = null;
        strMinSystole = null;
        strMaxSystole = null;
        strMinDistole = null;
        strMaxDistole = null;
        strMinSugarFPG = null;
        strMaxSugarFPG = null;
        strMinSugarPPG = null;
        strMaxSugarPPG = null;
        strMaxRespiration = null;
        strMinRespiration = null;
        strMinSpo2 = null;
        strMaxSpo2 = null;
        valMinSpo2 = null;
        valMaxSpo2 = null;
        spo2MinValue = null;
        spo2MaxValue = null;
        respirationMinValue = null;
        respirationMaxValue = null;
        strMinRespiration = null;
        strMaxRespiration = null;

    }

    private void reseFiltersData() {

        // seekbarWeight.setRangeValues(strMinWeight, strMaxWeight);// if we want to set progrmmatically set range of seekbar
        if (mAutoLabel != null) mAutoLabel.clear();
        if (mInvestigationLabel != null) mInvestigationLabel.clear();
        weightMinValue = null;
        weightMaxValue = null;
        heightMinValue = null;
        heightMaxValue = null;
        bmiMinValue = null;
        bmiMaxValue = null;
        pulseMaxValue = null;
        pulseMinValue = null;
        tempMinValue = null;
        tempMaxValue = null;
        systoleMinValue = null;
        systoleMaxValue = null;
        distoleMinValue = null;
        distoleMaxValue = null;
        sugarFpgMinValue = null;
        sugarFpgMaxValue = null;
        sugarPpgMinValue = null;
        sugarPpgMaxValue = null;
        strEcg = null;
        strPft = null;
        strAcer = null;
        strLipidHDL = null;
        strLipidLDL = null;
        strLipidTC = null;
        strLipidTG = null;
        strLipidVHDL = null;
        strSerumUrea = null;
        strEcg = null;
        strPft = null;
        strAcerMax = null;
        strHbA1c = null;
        strHbA1cMax = null;
        strLipidHDLMax = null;
        strLipidLDLMax = null;
        strLipidTCMax = null;
        strLipidTGMax = null;
        strLipidVHDLMax = null;
        strSerumUreaMax = null;
        healthDataBundle = null;
        strTobaco = null;
        strSmoking = null;
        strAlcoholConsumption = null;
        noOfSticksPerYear = null;
        noOfPegsPerYear = null;
        strLeftSmokingSinceYear = null;
        strLeftAlcoholSinceYear = null;
        strSleepStatus = null;
        strStressLevel = null;
        strSexuallyActive = null;
        strExcercise = null;
        strAllergie = null;
        strBingeEating = null;
        strLactoseTolerance = null;
        strLifeStyle = null;
        otherTobacoTaking = null;
        strDrug = null;
        otherDrugTaking = null;
        strFoodHabit = null;
        strFoodPreference = null;
        strPallore = null;
        strTremors = null;
        strIcterus = null;
        strClubbing = null;
        strOedema = null;
        strCalfTenderness = null;
        strLymphadenopathy = null;
        strPallorDescription = null;
        strCyanosisDescription = null;
        strTremorsDescription = null;
        strIcterusDescription = null;
        strClubbingDescription = null;
        strOedemaDescription = null;
        strCalfTendernessDescription = null;
        strLymphadenopathyDescription = null;

        strCyanosis = null;
        strTremors = null;
        strIcterus = null;
        strClubbing = null;
        strOedema = null;
        strCalfTenderness = null;
        strLymphadenopathy = null;
        valMinSpo2 = null;
        valMaxSpo2 = null;
        spo2MinValue = null;
        spo2MaxValue = null;
        respirationMinValue = null;
        respirationMaxValue = null;

    }

    private void openInvestigationDialog() {

        final Dialog dialog = new Dialog(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());

        final View f = factory.inflate(R.layout.investigation_dialognew, null);
        dialog.setTitle(" Filter Investigation ");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(f);

        final Button cancel = (Button) f.findViewById(R.id.customDialogCancel);
        final Button ok = (Button) f.findViewById(R.id.customDialogOk);
        final EditText input_hba1c = (EditText) f.findViewById(R.id.input_hba1c);
        final EditText input_acer = (EditText) f.findViewById(R.id.input_acer);
        final EditText input_seremUrea = (EditText) f.findViewById(R.id.input_seremUrea);
        final EditText input_LipidHDL = (EditText) f.findViewById(R.id.input_LipidHDL);
        final EditText input_LipidTC = (EditText) f.findViewById(R.id.input_LipidTC);
        final EditText input_LipidTG = (EditText) f.findViewById(R.id.input_LipidTG);
        final EditText input_LipidLDL = (EditText) f.findViewById(R.id.input_LipidLDL);
        final EditText input_LipidVHDL = (EditText) f.findViewById(R.id.input_LipidVHDL);

        input_min_sugarfpg = (EditText) f.findViewById(R.id.input_sugarfasting);
        input_max_sugarfpg = (EditText) f.findViewById(R.id.input_sugarfasting_max);
        input_min_sugarppg = (EditText) f.findViewById(R.id.input_sugar);
        input_max_sugarppg = (EditText) f.findViewById(R.id.input_sugar_max);

        final EditText input_hba1c_max = (EditText) f.findViewById(R.id.input_hba1c_max);
        final EditText input_acer_max = (EditText) f.findViewById(R.id.input_acer_max);
        final EditText input_seremUrea_max = (EditText) f.findViewById(R.id.input_seremUrea_max);
        final EditText input_LipidHDL_max = (EditText) f.findViewById(R.id.input_LipidHDL_max);
        final EditText input_LipidTC_max = (EditText) f.findViewById(R.id.input_LipidTC_max);
        final EditText input_LipidTG_max = (EditText) f.findViewById(R.id.input_LipidTG_max);
        final EditText input_LipidLDL_max = (EditText) f.findViewById(R.id.input_LipidLDL_max);
        final EditText input_LipidVHDL_max = (EditText) f.findViewById(R.id.input_LipidVHDL_max);


        final CheckBox cbPftNormal = (CheckBox) f.findViewById(R.id.cbPftNormal);
        final CheckBox cbPftAbnormal = (CheckBox) f.findViewById(R.id.cbPftAbnormal);

        final CheckBox cbEcgNormal = (CheckBox) f.findViewById(R.id.cbEcgNormal);
        final CheckBox cbEcgAbnormal = (CheckBox) f.findViewById(R.id.cbEcgAbnormal);

        if (sugarFpgMinValue != null && sugarFpgMaxValue != null) {
            input_min_sugarfpg.setText(sugarFpgMinValue.toString());
            input_max_sugarfpg.setText(sugarFpgMaxValue.toString());
        }
        if (sugarPpgMinValue != null && sugarPpgMaxValue != null) {
            input_min_sugarppg.setText(sugarPpgMinValue.toString());
            input_max_sugarppg.setText(sugarPpgMaxValue.toString());
        }

        if (strLipidTC != null && !strLipidTC.equals("")) {
            input_LipidTC.setText(strLipidTC);
        }
        if (strLipidTG != null && !strLipidTG.equals("")) {
            input_LipidTG.setText(strLipidTG);
        }
        if (strLipidLDL != null && !strLipidLDL.equals("")) {
            input_LipidLDL.setText(strLipidLDL);
        }
        if (strLipidVHDL != null && !strLipidVHDL.equals("")) {
            input_LipidVHDL.setText(strLipidVHDL);
        }
        if (strLipidHDL != null && !strLipidHDL.equals("")) {
            input_LipidHDL.setText(strLipidHDL);
        }
        if (strHbA1c != null) {
            input_hba1c.setText(strHbA1c.toString());
        }
        if (strSerumUrea != null && !strSerumUrea.equals("")) {
            input_seremUrea.setText(strSerumUrea);
        }
        if (strAcer != null && !strAcer.equals("")) {
            input_acer.setText(strAcer);
        }

        if (strLipidTCMax != null && !strLipidTCMax.equals("")) {
            input_LipidTC_max.setText(strLipidTCMax);
        }
        if (strLipidTGMax != null && !strLipidTGMax.equals("")) {
            input_LipidTG_max.setText(strLipidTGMax);
        }

        if (strLipidLDLMax != null && !strLipidLDLMax.equals("")) {
            input_LipidLDL_max.setText(strLipidLDLMax);
        }
        if (strLipidVHDLMax != null && !strLipidVHDLMax.equals("")) {
            input_LipidVHDL_max.setText(strLipidVHDLMax);
        }
        if (strLipidHDLMax != null && !strLipidHDLMax.equals("")) {
            input_LipidHDL_max.setText(strLipidHDLMax);
        }
        if (strHbA1cMax != null && !strHbA1cMax.equals("")) {
            input_hba1c_max.setText(strHbA1cMax.toString());
        }
        if (strSerumUreaMax != null && !strSerumUreaMax.equals("")) {
            input_seremUrea_max.setText(strSerumUreaMax);
        }
        if (strAcerMax != null && !strAcerMax.equals("")) {
            input_acer_max.setText(strAcerMax);
        }

        if (strEcg != null && !strEcg.equals(""))
            switch (strEcg) {
                case "Normal":

                    cbEcgNormal.setChecked(true);
                    break;
                case "Abnormal":

                    cbEcgAbnormal.setChecked(true);
                    break;
            }
        if (strPft != null && !strPft.equals(""))
            switch (strPft) {
                case "Normal":

                    cbPftNormal.setChecked(true);
                    break;
                case "Abnormal":

                    cbPftAbnormal.setChecked(true);
                    break;
            }

        cbEcgNormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbEcgNormal.isChecked()) {
                    strEcg = "Normal";
                    cbEcgAbnormal.setChecked(false);
                } else {
                    cbEcgNormal.setChecked(false);
                    strEcg = "";
                }
            }
        });
        cbEcgAbnormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbEcgAbnormal.isChecked()) {
                    strEcg = "Abnormal";
                    cbEcgNormal.setChecked(false);
                } else {
                    cbEcgAbnormal.setChecked(false);
                    strEcg = "";

                }
            }
        });


        cbPftNormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbPftNormal.isChecked()) {
                    strPft = "Normal";
                    cbPftAbnormal.setChecked(false);
                } else {
                    cbPftNormal.setChecked(false);
                    strPft = "";
                }
            }
        });
        cbPftAbnormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbPftAbnormal.isChecked()) {
                    strPft = "Abnormal";
                    cbPftNormal.setChecked(false);
                } else {
                    cbPftAbnormal.setChecked(false);
                    strPft = "";

                }
            }
        });


        cancel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_color));
                    dialog.dismiss();
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                }
                return false;
            }

        });

        ok.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    ok.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ok.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInvestigationLabel.clear();

                String strMinHbA1c = input_hba1c.getText().toString();

                String strMaxHbA1c = input_hba1c_max.getText().toString();

                if (strMinHbA1c != null && !strMinHbA1c.equals("") && strMaxHbA1c == null || strMaxHbA1c.equals("")) {

                    strMaxHbA1c = String.valueOf(mMaxHbA1c);

                } else if (strMaxHbA1c != null && !strMaxHbA1c.equals("") && strMinHbA1c == null || strMinHbA1c.equals("")) {

                    strMinHbA1c = String.valueOf(mMinHbA1c);

                } else if (strMinHbA1c == null && strMaxHbA1c == null) {
                    strMinHbA1c = null;
                    strMaxHbA1c = null;
                }

                if (strMinHbA1c != null && strMaxHbA1c != null && !strMinHbA1c.equals("") && !strMaxHbA1c.equals("")) {

                    strHbA1c = Integer.valueOf(strMinHbA1c);
                    strHbA1cMax = Integer.valueOf(strMaxHbA1c);

                    if (strHbA1cMax < strHbA1c) {
                        input_hba1c_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (strHbA1c > strHbA1cMax) {
                        input_hba1c.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (strHbA1c > strHbA1cMax) {
                        input_hba1c.setError("The min value recored in your database is  " + mMaxHbA1c);
                        return;
                    } else if (strHbA1cMax > Integer.valueOf(strMaxHbA1c)) {
                        input_hba1c_max.setError("The max value recored in your database is  " + mMaxHbA1c);
                        return;
                    }
                }
                if (!strMinHbA1c.isEmpty() && !strMaxHbA1c.isEmpty()) {

                    mInvestigationLabel.addLabel("HbA1c " + strMinHbA1c + " - " + strMaxHbA1c);
                }
                String strMinAcer = input_acer.getText().toString();
                String strMaxAcer = input_acer_max.getText().toString();

                if (strMinAcer != null && !strMinAcer.equals("") && strMaxAcer == null || strMaxAcer.equals("")) {

                    strMaxAcer = String.valueOf(mMaxAcer);

                } else if (strMaxAcer != null && !strMaxAcer.equals("") && strMinAcer == null || strMinAcer.equals("")) {

                    strMinAcer = String.valueOf(mMinAcer);

                } else if (strMinHbA1c == null && strMaxAcer == null) {
                    strMinAcer = null;
                    strMaxAcer = null;

                }
                if (strMinAcer != null && strMaxAcer != null && !strMinAcer.equals("") && !strMaxAcer.equals("")) {
                    int acerMinValue = Integer.valueOf(strMinAcer);
                    int acerMaxValue = Integer.valueOf(strMaxAcer);

                    if (acerMaxValue < acerMinValue) {
                        input_acer_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (acerMinValue > acerMaxValue) {
                        input_acer.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (acerMaxValue > mMaxAcer) {
                        input_acer_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxAcer);
                        return;
                    }
                }
                if (strMinAcer != null && strMaxAcer != null && !strMinAcer.equals("") && !strMaxAcer.equals("")) {

                    strAcer = strMinAcer;
                    strAcerMax = strMaxAcer;
                }
                if (!strMinAcer.isEmpty() && !strMaxAcer.isEmpty()) {

                    mInvestigationLabel.addLabel("ACR " + strMinAcer + " - " + strMaxAcer);
                }
                String strMinLipidHDL = input_LipidHDL.getText().toString();
                String strMaxLipidHDL = input_LipidHDL_max.getText().toString();

                if (strMinLipidHDL != null && !strMinLipidHDL.equals("") && strMaxLipidHDL == null || strMaxLipidHDL.equals("")) {

                    strMaxLipidHDL = String.valueOf(mMaxHdl);

                } else if (strMaxLipidHDL != null && !strMaxLipidHDL.equals("") && strMinLipidHDL == null || strMinLipidHDL.equals("")) {

                    strMinLipidHDL = String.valueOf(mMinHdl);

                } else if (strMinLipidHDL == null && strMaxLipidHDL == null) {
                    strMinLipidHDL = null;
                    strMaxLipidHDL = null;

                }

                if (strMinLipidHDL != null && strMaxLipidHDL != null && !strMinLipidHDL.equals("") && !strMaxLipidHDL.equals("")) {
                    int hdlMinValue = Integer.valueOf(strMinLipidHDL);
                    int hdlMaxValue = Integer.valueOf(strMaxLipidHDL);

                    if (hdlMaxValue < hdlMinValue) {
                        input_LipidHDL_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (hdlMinValue > hdlMaxValue) {
                        input_LipidHDL.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (hdlMaxValue > mMaxHdl) {
                        input_LipidHDL_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxHdl);
                        return;
                    }
                }
                if (strMaxLipidHDL != null && strMinLipidHDL != null && !strMaxLipidHDL.equals("") && !strMinLipidHDL.equals("")) {

                    strLipidHDL = strMinLipidHDL;
                    strLipidHDLMax = strMaxLipidHDL;
                }
                if (!strMinLipidHDL.isEmpty() && !strMaxLipidHDL.isEmpty()) {

                    mInvestigationLabel.addLabel("HDL " + strMinLipidHDL + " - " + strMaxLipidHDL);
                }
                String strMinLipidLDL = input_LipidLDL.getText().toString();
                String strMaxLipidLDL = input_LipidLDL_max.getText().toString();


                if (strMinLipidLDL != null && !strMinLipidLDL.equals("") && strMaxLipidLDL == null || strMaxLipidLDL.equals("")) {

                    strMaxLipidLDL = String.valueOf(mMaxLdl);

                } else if (strMaxLipidLDL != null && !strMaxLipidLDL.equals("") && strMinLipidLDL == null || strMinLipidLDL.equals("")) {

                    strMinLipidLDL = String.valueOf(mMinLdl);

                } else if (strMinLipidLDL == null && strMaxLipidLDL == null) {
                    strMinLipidLDL = null;
                    strMaxLipidLDL = null;

                }
                if (strMinLipidLDL != null && strMaxLipidLDL != null && !strMinLipidLDL.equals("") && !strMaxLipidLDL.equals("")) {
                    int ldlMinValue = Integer.valueOf(strMinLipidLDL);
                    int ldlMaxValue = Integer.valueOf(strMaxLipidLDL);

                    if (ldlMaxValue < ldlMinValue) {
                        input_LipidLDL_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (ldlMinValue > ldlMaxValue) {
                        input_LipidLDL.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (ldlMaxValue > mMaxLdl) {
                        input_LipidLDL_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxLdl);
                        return;
                    }
                }
                if (strMinLipidLDL != null && strMaxLipidLDL != null && !strMinLipidLDL.equals("") && !strMaxLipidLDL.equals("")) {

                    strLipidLDL = strMinLipidLDL;
                    strLipidLDLMax = strMaxLipidLDL;
                }
                if (!strMinLipidLDL.isEmpty() && !strMaxLipidLDL.isEmpty()) {

                    mInvestigationLabel.addLabel("LDL " + strMinLipidLDL + " - " + strMaxLipidLDL);
                }

                String strMinTC = input_LipidTC.getText().toString();
                String strMaxTC = input_LipidTC_max.getText().toString();


                if (strMinTC != null && !strMinTC.equals("") && strMaxTC == null || strMaxTC.equals("")) {

                    strMaxTC = String.valueOf(mMaxTc);

                } else if (strMaxTC != null && !strMaxTC.equals("") && strMinTC == null || strMinTC.equals("")) {

                    strMinTC = String.valueOf(mMinTc);

                } else if (strMinTC == null && strMaxTC == null) {
                    strMinTC = null;
                    strMaxTC = null;

                }

                if (strMinTC != null && strMaxTC != null && !strMinTC.equals("") && !strMaxTC.equals("")) {
                    int tcMinValue = Integer.valueOf(strMinTC);
                    int tcMaxValue = Integer.valueOf(strMaxTC);

                    if (tcMaxValue < tcMinValue) {
                        input_LipidTC_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (tcMinValue > tcMaxValue) {
                        input_LipidTC.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (tcMaxValue > mMaxTc) {
                        input_LipidTC_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxTc);
                        return;
                    }
                }
                if (strMinTC != null && strMaxTC != null && !strMinTC.equals("") && !strMaxTC.equals("")) {

                    strLipidTC = strMinTC;
                    strLipidTCMax = strMaxTC;
                }
                if (!strMinTC.isEmpty() && !strMaxTC.isEmpty()) {

                    mInvestigationLabel.addLabel("TC " + strMinTC + " - " + strMaxTC);
                }
                String strMinTG = input_LipidTG.getText().toString();
                String strMaxTG = input_LipidTG_max.getText().toString();

                if (strMinTG != null && !strMinTG.equals("") && strMaxTG == null || strMaxTG.equals("")) {

                    strMaxTG = String.valueOf(mMaxTg);

                } else if (strMaxTG != null && !strMaxTG.equals("") && strMinTG == null || strMinTG.equals("")) {

                    strMinTG = String.valueOf(mMinTg);

                } else if (strMaxTG == null && strMinTG == null) {
                    strMinTG = null;
                    strMaxTG = null;

                }

                if (strMinTG != null && strMaxTG != null && !strMinTG.equals("") && !strMaxTG.equals("")) {

                    int tgMinValue = Integer.valueOf(strMinTG);
                    int tgMaxValue = Integer.valueOf(strMaxTG);

                    if (tgMaxValue < tgMinValue) {
                        input_LipidTG_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (tgMinValue > tgMaxValue) {
                        input_LipidTG.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (tgMaxValue > mMaxTg) {
                        input_LipidTG_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxTg);
                        return;
                    }
                }
                if (strMinTG != null && strMaxTG != null && !strMinTG.equals("") && !strMaxTG.equals("")) {

                    strLipidTG = strMinTG;
                    strLipidTGMax = strMaxTG;
                }
                if (!strMinTG.isEmpty() && !strMaxTG.isEmpty()) {

                    mInvestigationLabel.addLabel("TG " + strMinTG + " - " + strMaxTG);
                }
                String strMinLipidVHDL = input_LipidVHDL.getText().toString();
                String strMaxLipidVHDLMax = input_LipidVHDL_max.getText().toString();

                if (strMinLipidVHDL != null && !strMinLipidVHDL.equals("") && strMaxLipidVHDLMax == null || strMaxLipidVHDLMax.equals("")) {

                    strMaxLipidVHDLMax = String.valueOf(mMaxVhdl);

                } else if (strMaxLipidVHDLMax != null && !strMaxLipidVHDLMax.equals("") && strMinLipidVHDL == null || strMinLipidVHDL.equals("")) {

                    strMinLipidVHDL = String.valueOf(mMinVhdl);

                } else if (strMaxLipidVHDLMax == null && strMinLipidVHDL == null) {
                    strMinLipidVHDL = null;
                    strMaxLipidVHDLMax = null;
                }
                if (strMinLipidVHDL != null && strMaxLipidVHDLMax != null && !strMinLipidVHDL.equals("") && !strMaxLipidVHDLMax.equals("")) {
                    int vhdlMinValue = Integer.valueOf(strMinLipidVHDL);
                    int vhdlMaxValue = Integer.valueOf(strMaxLipidVHDLMax);

                    if (vhdlMaxValue < vhdlMinValue) {
                        input_LipidVHDL_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (vhdlMinValue > vhdlMaxValue) {
                        input_LipidVHDL.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (vhdlMaxValue > mMaxVhdl) {
                        input_LipidVHDL_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxVhdl);
                        return;
                    }
                }
                if (strMinLipidVHDL != null && strMaxLipidVHDLMax != null && !strMinLipidVHDL.equals("") && !strMaxLipidVHDLMax.equals("")) {

                    strLipidVHDL = strMinLipidVHDL;
                    strLipidVHDLMax = strMaxLipidVHDLMax;
                }
                if (!strMinLipidVHDL.isEmpty() && !strMaxLipidVHDLMax.isEmpty()) {

                    mInvestigationLabel.addLabel("VLDL " + strMinLipidVHDL + " - " + strMaxLipidVHDLMax);
                }

                String strMinSerumUrea = input_seremUrea.getText().toString();
                String strMaxSerumUrea = input_seremUrea_max.getText().toString();

                if (strMinSerumUrea != null && !strMinSerumUrea.equals("") && strMaxSerumUrea == null || strMaxSerumUrea.equals("")) {

                    strMaxSerumUrea = String.valueOf(mMaxSerumUrea);

                } else if (strMaxSerumUrea != null && !strMaxSerumUrea.equals("") && strMinSerumUrea == null || strMinSerumUrea.equals("")) {

                    strMinSerumUrea = String.valueOf(mMinSerumUrea);

                } else if (strMaxSerumUrea == null && strMinSerumUrea == null) {
                    strMinSerumUrea = null;
                    strMaxSerumUrea = null;

                }

                if (strMinSerumUrea != null && strMaxSerumUrea != null && !strMinSerumUrea.equals("") && !strMaxSerumUrea.equals("")) {
                    int seremMinValue = Integer.valueOf(strMinSerumUrea);
                    int seremMaxValue = Integer.valueOf(strMaxSerumUrea);

                    if (seremMaxValue < seremMinValue) {
                        input_seremUrea_max.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (seremMinValue > seremMaxValue) {
                        input_seremUrea.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (seremMaxValue > mMaxSerumUrea) {
                        input_seremUrea_max.setError(getResources().getString(R.string.maxgreatdbmax) + " " + mMaxSerumUrea);
                        return;
                    }
                }
                if (strMinSerumUrea != null && strMaxSerumUrea != null && !strMinSerumUrea.equals("") && !strMaxSerumUrea.equals("")) {

                    strSerumUrea = strMinSerumUrea;
                    strSerumUreaMax = strMaxSerumUrea;
                }
                if (!strMinSerumUrea.isEmpty() && !strMaxSerumUrea.isEmpty()) {

                    mInvestigationLabel.addLabel("SerumUrea " + strMinSerumUrea + " - " + strMaxSerumUrea);
                }
                if (strEcg != null && !strEcg.isEmpty() && !strEcg.equals("")) {
                    mInvestigationLabel.addLabel("ECG - " + strEcg);
                }
                if (strPft != null && !strPft.isEmpty() && !strPft.equals("")) {
                    mInvestigationLabel.addLabel("PFT - " + strPft);
                }
                String valMinSugarFpg = input_min_sugarfpg.getText().toString();
                String valMaxSugarFpg = input_max_sugarfpg.getText().toString();

                if (valMinSugarFpg != null && valMaxSugarFpg == null || valMaxSugarFpg.equals("")) {
                    valMaxSugarFpg = String.valueOf(strMaxSugarFPG);
                } else if (valMaxSugarFpg != null && valMinSugarFpg == null || valMinSugarFpg.equals("")) {
                    valMinSugarFpg = String.valueOf(strMinSugarFPG);
                } else if (valMinSugarFpg == null && valMaxSugarFpg == null) {
                    valMinSugarFpg = null;
                    valMaxSugarFpg = null;
                }
                if (valMinSugarFpg != null && valMaxSugarFpg != null && !valMinSugarFpg.equals("") && !valMaxSugarFpg.equals("")) {

                    sugarFpgMinValue = Integer.valueOf(valMinSugarFpg);
                    sugarFpgMaxValue = Integer.valueOf(valMaxSugarFpg);

                    if (sugarFpgMaxValue < sugarFpgMinValue) {
                        input_max_sugarfpg.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (sugarFpgMinValue > sugarFpgMaxValue) {
                        input_min_sugarfpg.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (sugarFpgMaxValue > strMaxSugarFPG) {
                        input_max_sugarfpg.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxSugarFPG);
                        return;
                    }
                }
                if (!valMinSugarFpg.isEmpty() && !valMaxSugarFpg.isEmpty()) {

                    mInvestigationLabel.addLabel("SugarFpg " + valMinSugarFpg + "-" + valMaxSugarFpg);
                }
                String valMinSugarPpg = input_min_sugarppg.getText().toString();
                String valMaxSugarPpg = input_max_sugarppg.getText().toString();

                if (valMinSugarPpg != null && valMaxSugarPpg == null || valMaxSugarPpg.equals("")) {
                    valMaxSugarPpg = String.valueOf(strMaxSugarPPG);
                } else if (valMaxSugarFpg != null && valMinSugarPpg == null || valMinSugarPpg.equals("")) {
                    valMinSugarPpg = String.valueOf(strMinSugarPPG);
                } else if (valMinSugarPpg == null && valMaxSugarPpg == null) {
                    valMinSugarPpg = null;
                    valMaxSugarPpg = null;

                }
                if (valMinSugarPpg != null && valMaxSugarPpg != null && !valMinSugarPpg.equals("") && !valMaxSugarPpg.equals("")) {

                    sugarPpgMinValue = Integer.valueOf(valMinSugarPpg);
                    sugarPpgMaxValue = Integer.valueOf(valMaxSugarPpg);

                    if (sugarPpgMaxValue < sugarPpgMinValue) {
                        input_max_sugarppg.setError(getResources().getText(R.string.minmaxvalid));
                        return;
                    } else if (sugarPpgMinValue > sugarPpgMaxValue) {
                        input_min_sugarppg.setError(getResources().getString(R.string.mingreatmax));
                        return;
                    } else if (sugarPpgMaxValue > strMaxSugarPPG) {
                        input_max_sugarppg.setError(getResources().getString(R.string.maxgreatdbmax) + " " + strMaxSugarPPG);
                        return;
                    }
                }
                if (!valMinSugarPpg.isEmpty() && !valMaxSugarPpg.isEmpty()) {

                    mInvestigationLabel.addLabel("SugarPpg " + valMinSugarPpg + "-" + valMaxSugarPpg);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showObservationsDialog() {

        final Dialog dialog;

        dialog = new Dialog(getContext());
        final LayoutInflater factory = LayoutInflater.from(getContext());

        final View f = factory.inflate(R.layout.observation_dialog_new, null);
        dialog.setTitle("Filter Observations");
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(f);

        final Button cancel = (Button) f.findViewById(R.id.customDialogCancel);
        final Button ok = (Button) f.findViewById(R.id.customDialogOk);

        final CheckBox cbPalloreYes = (CheckBox) f.findViewById(R.id.cbPalloreYes);
        final CheckBox cbPalloreNo = (CheckBox) f.findViewById(R.id.cbPalloreNo);

        final CheckBox cbCyanosiYes = (CheckBox) f.findViewById(R.id.cbCyanosiYes);
        final CheckBox cbCyanosisNo = (CheckBox) f.findViewById(R.id.cbCyanosiNo);
        final CheckBox cbTremorsYes = (CheckBox) f.findViewById(R.id.cbTremorsYes);
        final CheckBox cbTremorsNo = (CheckBox) f.findViewById(R.id.cbTremorsNo);

        final CheckBox cbIcterusYes = (CheckBox) f.findViewById(R.id.cbIcterusYes);
        final CheckBox cbIcterusNo = (CheckBox) f.findViewById(R.id.cbIcterusNo);
        final CheckBox cbClubbingYes = (CheckBox) f.findViewById(R.id.cbClubbingYes);
        final CheckBox cbClubbingNo = (CheckBox) f.findViewById(R.id.cbClubbingNo);

        final CheckBox cbOedemaYes = (CheckBox) f.findViewById(R.id.cbOedemaYes);
        final CheckBox cbOedemaNo = (CheckBox) f.findViewById(R.id.cbOedemaNo);
        final CheckBox cbCalfTendernessYes = (CheckBox) f.findViewById(R.id.cbCalfTendernessYes);
        final CheckBox cbCalfTendernessNo = (CheckBox) f.findViewById(R.id.cbCalfTendernessNo);

        final CheckBox cbLymphadenopathyYes = (CheckBox) f.findViewById(R.id.cbLymphadenopathyYes);
        final CheckBox cbLymphadenopathyNo = (CheckBox) f.findViewById(R.id.cbLymphadenopathyNo);

        final EditText pallorDescription = (EditText) f.findViewById(R.id.pallorDescription);
        final EditText cyanosisDescription = (EditText) f.findViewById(R.id.cyanosisDescription);
        final EditText tremorsDescription = (EditText) f.findViewById(R.id.tremorsDescription);
        final EditText icterusDescription = (EditText) f.findViewById(R.id.icterusDescription);
        final EditText clubbingDescription = (EditText) f.findViewById(R.id.clubbingDescription);
        final EditText oedemaDescription = (EditText) f.findViewById(R.id.oedemaDescription);
        final EditText calfTendernessDescription = (EditText) f.findViewById(R.id.calfTendernessDescription);
        final EditText lymphadenopathyDescription = (EditText) f.findViewById(R.id.lymphadenopathyDescription);

        if (strPallorDescription != null) pallorDescription.setText(strPallorDescription);
        if (strCyanosisDescription != null) cyanosisDescription.setText(strCyanosisDescription);
        if (strTremorsDescription != null) tremorsDescription.setText(strTremorsDescription);
        if (strIcterusDescription != null) icterusDescription.setText(strIcterusDescription);
        if (strClubbingDescription != null) clubbingDescription.setText(strClubbingDescription);
        if (strOedemaDescription != null) oedemaDescription.setText(strOedemaDescription);
        if (strCalfTendernessDescription != null)
            calfTendernessDescription.setText(strCalfTendernessDescription);
        if (strLymphadenopathyDescription != null)
            lymphadenopathyDescription.setText(strLymphadenopathyDescription);

        if (strPallore != null && !strPallore.equals(""))
            switch (strPallore) {
                case "Yes":
                    cbPalloreYes.setChecked(true);

                    break;
                case "No":
                    cbPalloreNo.setChecked(true);
                    break;

            }
        if (strCyanosis != null && !strCyanosis.equals(""))
            switch (strCyanosis) {
                case "Yes":
                    cbCyanosiYes.setChecked(true);
                    break;
                case "No":
                    cbCyanosisNo.setChecked(true);
                    break;
            }
        if (strTremors != null && !strTremors.equals(""))
            switch (strTremors) {
                case "Yes":
                    cbTremorsYes.setChecked(true);
                    break;
                case "No":
                    cbTremorsNo.setChecked(true);
                    break;
            }
        if (strIcterus != null && !strIcterus.equals(""))
            switch (strIcterus) {
                case "Yes":
                    cbIcterusYes.setChecked(true);

                    break;
                case "No":
                    cbIcterusNo.setChecked(true);

                    break;
            }

        if (strClubbing != null && !strClubbing.equals(""))
            switch (strClubbing) {
                case "Yes":
                    cbClubbingYes.setChecked(true);
                    break;
                case "No":
                    cbClubbingNo.setChecked(true);
                    break;
            }
        if (strOedema != null && !strOedema.equals(""))
            switch (strOedema) {
                case "Yes":
                    cbOedemaYes.setChecked(true);

                    break;
                case "No":
                    cbOedemaNo.setChecked(true);
                    break;
            }
        if (strCalfTenderness != null && !strCalfTenderness.equals(""))
            switch (strCalfTenderness) {
                case "Yes":
                    cbCalfTendernessYes.setChecked(true);

                    break;
                case "No":
                    cbCalfTendernessNo.setChecked(true);
                    break;
            }

        if (strLymphadenopathy != null && !strLymphadenopathy.equals(""))
            switch (strLymphadenopathy) {

                case "Yes":
                    cbLymphadenopathyYes.setChecked(true);

                    break;
                case "No":
                    cbLymphadenopathyNo.setChecked(true);

                    break;
            }

        cbPalloreYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbPalloreYes.isChecked()) {
                    strPallore = "Yes";
                    cbPalloreNo.setChecked(false);
                } else {
                    cbPalloreYes.setChecked(false);
                    strPallore = "";
                }
            }
        });
        cbPalloreNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbPalloreNo.isChecked()) {
                    strPallore = "No";
                    cbPalloreYes.setChecked(false);
                } else {
                    cbPalloreNo.setChecked(false);
                    strPallore = "";

                }
            }
        });

        cbCyanosiYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbCyanosiYes.isChecked()) {

                    strCyanosis = "Yes";
                    cbCyanosisNo.setChecked(false);
                } else {
                    cbCyanosiYes.setChecked(false);
                    strCyanosis = "";

                }
            }
        });
        cbCyanosisNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbCyanosisNo.isChecked()) {

                    strCyanosis = "No";
                    cbCyanosiYes.setChecked(false);
                } else {
                    cbCyanosisNo.setChecked(false);
                    strCyanosis = "";

                }
            }
        });

        cbTremorsYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbTremorsYes.isChecked()) {

                    strTremors = "Yes";
                    cbTremorsNo.setChecked(false);
                } else {
                    cbTremorsYes.setChecked(false);
                    strTremors = "";

                }
            }
        });
        cbTremorsNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbTremorsNo.isChecked()) {

                    strTremors = "No";
                    cbTremorsYes.setChecked(false);
                } else {
                    cbTremorsNo.setChecked(false);
                    strTremors = "";

                }
            }
        });
        cbIcterusYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbIcterusYes.isChecked()) {

                    strIcterus = "Yes";
                    cbIcterusNo.setChecked(false);
                } else {
                    cbIcterusYes.setChecked(false);
                    strIcterus = "";

                }
            }
        });
        cbIcterusNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbIcterusNo.isChecked()) {

                    strIcterus = "No";
                    cbIcterusYes.setChecked(false);
                } else {
                    cbIcterusNo.setChecked(false);
                    strIcterus = "";

                }
            }
        });

        cbClubbingYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbClubbingYes.isChecked()) {

                    strClubbing = "Yes";
                    cbClubbingNo.setChecked(false);
                } else {
                    cbClubbingYes.setChecked(false);
                    strClubbing = "";

                }
            }
        });
        cbClubbingNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbClubbingNo.isChecked()) {

                    strClubbing = "No";
                    cbClubbingYes.setChecked(false);
                } else {
                    cbClubbingNo.setChecked(false);
                    strClubbing = "";

                }
            }
        });

        cbOedemaYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbOedemaYes.isChecked()) {

                    strOedema = "Yes";
                    cbOedemaNo.setChecked(false);
                } else {
                    cbOedemaYes.setChecked(false);
                    strOedema = "";

                }
            }
        });
        cbOedemaNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbOedemaNo.isChecked()) {

                    strOedema = "No";
                    cbOedemaYes.setChecked(false);
                } else {
                    cbOedemaNo.setChecked(false);
                    strOedema = "";

                }
            }
        });

        cbCalfTendernessYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbCalfTendernessYes.isChecked()) {

                    strCalfTenderness = "Yes";
                    cbCalfTendernessNo.setChecked(false);
                } else {
                    cbCalfTendernessYes.setChecked(false);
                    strCalfTenderness = "";

                }
            }
        });
        cbCalfTendernessNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbCalfTendernessNo.isChecked()) {

                    strCalfTenderness = "No";
                    cbCalfTendernessYes.setChecked(false);
                } else {
                    cbCalfTendernessNo.setChecked(false);
                    strCalfTenderness = "";

                }
            }
        });

        cbLymphadenopathyYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbLymphadenopathyYes.isChecked()) {

                    strLymphadenopathy = "Yes";
                    cbLymphadenopathyNo.setChecked(false);
                } else {
                    cbLymphadenopathyYes.setChecked(false);
                    strLymphadenopathy = "";

                }
            }
        });
        cbLymphadenopathyNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cbLymphadenopathyNo.isChecked()) {

                    strLymphadenopathy = "No";
                    cbLymphadenopathyYes.setChecked(false);
                } else {
                    cbLymphadenopathyNo.setChecked(false);
                    strLymphadenopathy = "";

                }
            }
        });

        cancel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));

                    dialog.dismiss();

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    cancel.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));
                }
                return false;
            }

        });


        ok.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    ok.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground));

                    strPallorDescription = pallorDescription.getText().toString();
                    strCyanosisDescription = cyanosisDescription.getText().toString();
                    strTremorsDescription = tremorsDescription.getText().toString();
                    strIcterusDescription = icterusDescription.getText().toString();
                    strClubbingDescription = clubbingDescription.getText().toString();
                    strOedemaDescription = oedemaDescription.getText().toString();
                    strCalfTendernessDescription = calfTendernessDescription.getText().toString();
                    strLymphadenopathyDescription = lymphadenopathyDescription.getText().toString();

                    dialog.dismiss();

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    ok.setBackground(getResources().getDrawable(R.drawable.rounded_corner_withbackground_blue));
                }
                return false;
            }

        });


        dialog.show();

    }

    private void setAutoLabelUISettings() {

        AutoLabelUISettings autoLabelUISettings =
                new AutoLabelUISettings.Builder()
                        .withBackgroundResource(R.color.colorPrimaryDark)
                        .withIconCross(R.drawable.cross)
                        .withMaxLabels(200)
                        .withShowCross(false)
                        .withLabelsClickables(false)
                        .withTextColor(android.R.color.white)
                        .withTextSize(R.dimen.label_title_size)
                        .withLabelPadding(10)
                        .build();

        mAutoLabel.setSettings(autoLabelUISettings);
        mInvestigationLabel.setSettings(autoLabelUISettings);

    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onLabelsCompleted() {

            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(View view, int position) {

            }

           /* @Override
            public void onRemoveLabel(Label removedLabel, int position) {
                Toast.makeText(getApplicationContext(),"Label with text \" " + removedLabel.getTag() + "\" has been removed.",Toast.LENGTH_LONG).show();
               // Snackbar.make(removedLabel, "Label with text \" " + removedLabel.getTag() + "\" has been removed.", Snackbar.LENGTH_SHORT).show();
            }*/
        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onLabelsEmpty() {


            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(View v) {


            }


        });
    }


    public File createPDF(ArrayList<RegistrationModel> list) throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        File dir, actFile = null;
        String path;
        //use to set background color
        PdfPCell cell;
        Image bgImage;
        FileOutputStream fOut;
        //creating new file path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CLIRNet/PDF Files";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            Log.e("PDFCreator", "PDF Path: " + path);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
            actFile = new File(dir, "CLIRNet" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            fOut = new FileOutputStream(actFile);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            //create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getContext().getResources().getDrawable(R.drawable.cliricon);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Patient Data List"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(9);

                float[] columnWidth = new float[]{20, 30, 40, 30, 30, 40, 40, 40, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);


                cell = new PdfPCell(new Phrase("#"));

                table.addCell(cell);
               /* cell = new PdfPCell(new Phrase("ID"));

                table.addCell(cell);*/
                cell = new PdfPCell(new Phrase("FIRST NAME"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("MIDDLE NAME"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("LAST NAME"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("AGE"));
                table.addCell("wrap");
                cell.setNoWrap(false);

                table.addCell(cell);
                cell = new PdfPCell(new Phrase("GENDER"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("MOBILE NUMBER"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("LANGUAGE"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("EMAIL"));
                table.addCell("wrap");
                cell.setNoWrap(false);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(6);
                int size = list.size();
                Log.e("size", "" + size);

                for (int i = 0; i < size; i++) {

                    RegistrationModel registrationModel = list.get(i);
                    table.addCell(String.valueOf(i));
                    //table.addCell(registrationModel.getId());
                    table.addCell(registrationModel.getFirstName());
                    table.addCell(registrationModel.getMiddleName());
                    table.addCell(registrationModel.getLastName());
                    table.addCell(registrationModel.getAge());
                    table.addCell(registrationModel.getGender());
                    table.addCell(registrationModel.getMobileNumber());
                    table.addCell(registrationModel.getLanguage());
                    table.addCell(registrationModel.getEmail());
                }

                PdfPTable ftable = new PdfPTable(9);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{30, 30, 40, 30, 30, 30, 40, 30, 30};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(6);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase("Total Number " + size));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);

                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Footer"));
                cell.setColspan(6);
                ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);
                Phrase  p = new Phrase("iText in Action Second Edition");
                cell.setPadding(2);
                cell.setUseAscender(true);
                cell.setUseDescender(true);
                table.addCell(p);
                doc.add(table);
                Toast.makeText(getContext().getApplicationContext(), "created PDF", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actFile;
    }

    private void exportDataDialog(final int positon) {
        final Dialog dialog = new Dialog(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View f = factory.inflate(R.layout.export_data_dialog, null);
         // final int newPosition;
        /*if(positon!=null && !positon.equals("")){
            final int  newPosition= Integer.parseInt(positon);
        }*/

        dialog.setTitle("Do you want to export patient data?");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(f);
        Button singelExport = (Button) f.findViewById(R.id.single_export);
        Button allExport = (Button) f.findViewById(R.id.all_export);
        RadioGroup genderbutton = (RadioGroup) f.findViewById(R.id.radioExport);


        genderbutton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioCsv:
                        //Toast.makeText(getContext(),"Male Selected",Toast.LENGTH_LONG).show();
                        exportChoice = "CSV";
                        break;
                    case R.id.radioPdf:
                        exportChoice = "PDF";
                        break;
                }
            }
        });
        singelExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDB(positon,exportChoice);
                dialog.dismiss();
            }
        });
        allExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    exportDB(exportChoice);
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
}



