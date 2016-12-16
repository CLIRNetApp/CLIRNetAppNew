package app.clirnet.com.clirnetapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.clirnet.com.clirnetapp.R;
import app.clirnet.com.clirnetapp.activity.NavigationActivity;
import app.clirnet.com.clirnetapp.app.AppController;
import app.clirnet.com.clirnetapp.helper.SQLController;


/**
 * Created by ${Ashish} on 22-04-2016.
 */
public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int DATE_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID1 = 1;
    private SQLController sqlController;
    private AppController appController;
    private OnFragmentInteractionListener mListener;

    private String date;
    private ArrayList<String> nocountsperday;
    ViewPager viewPager;
    TabLayout tabLayout;
    private EditText fromdate;
    private EditText todate;

    private int year;
    private int month;
    private int day;
    private int cur = 0;
    private ImageView filterDate;
    private Button lastweek;
    private Button lastmonth;
    private Button lastquarter;
    private android.support.v4.app.FragmentManager mFragmentManager;
    private String startDate, endDate;
    private ReportFragmentViewPagerSetup fragment;
    private View rootview;
    private long diffInHours;


    //this is of no use
    public ReportFragment() {
        this.setHasOptionsMenu(true);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        rootview = inflater.inflate(R.layout.fragment_reports, container, false);
        ((NavigationActivity) getActivity()).setActionBarTitle("Reports");

        fromdate = (EditText) rootview.findViewById(R.id.fromdate);
        todate = (EditText) rootview.findViewById(R.id.todate);

        lastweek = (Button) rootview.findViewById(R.id.lastweek);
        lastmonth = (Button) rootview.findViewById(R.id.lastmonth);
        lastquarter = (Button) rootview.findViewById(R.id.lastquarter);

        viewPager = (ViewPager) rootview.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);

        setCurrentDateOnView("1");//set dates from todays to -7 days to edit text
        addListenerOnButton();

        getDateValues();
        //this will by default set reports for the week from todays date

        mFragmentManager = getChildFragmentManager();
        CallLastWeek();

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        appController = new AppController();

        filterDate = (ImageView) rootview.findViewById(R.id.filterDate);
        filterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDateValues();


                String format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                try {
                    Date fromDate = sdf.parse(startDate);
                    Date toDate = sdf.parse(endDate);


                    long duration = toDate.getTime() - fromDate.getTime();


                    diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

                    Log.e("diffInHours", "       " + diffInHours);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");


                String reformattedStrDate = "";
                String reformatedToDate = "";
                try {

                    reformattedStrDate = myFormat.format(fromUser.parse(startDate));
                    reformatedToDate = myFormat.format(fromUser.parse(endDate));


                } catch (ParseException e) {
                    e.printStackTrace();
                    appController.appendLog(appController.getDateTime() + " " + "/ " + "Add Patient" + e);
                }

                if (diffInHours <= 0) {

                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "To-Date Must Be Greater Than From-Date", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    return;
                }

                // todo get data from db
                ReportFragmentViewPagerSetup fragment = new ReportFragmentViewPagerSetup();
                Bundle b = new Bundle();
                b.putString("FROMDATE", reformattedStrDate);
                b.putString("TODATE", reformatedToDate);
                fragment.setArguments(b);

                mFragmentManager.beginTransaction()
                        .replace(R.id.framecontainer, fragment).addToBackStack("true")
                        .commit();


            }
        });
        lastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  date = appController.addDay(new Date(), -7);
                fromdate.setText(date);*/

                setCurrentDateOnView("1"); //set dates from todays to -7 days to edit text


                getDateValues();
                CallLastWeek();

                /*ReportFragmentViewPagerSetup
                        fragment = new ReportFragmentViewPagerSetup();
                Bundle b = new Bundle();
                b.putString("FROMDATE", startDate);
                b.putString("TODATE", endDate);
                fragment.setArguments(b);
                mFragmentManager.beginTransaction()
                        .replace(R.id.framecontainer, fragment).commit();*/
            }
        });

        lastmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                date = appController.addDay(new Date(), -30);
                fromdate.setText(date);*/
                setCurrentDateOnView("2");//set dates from todays to -30 days to edit text

                getDateValues();

                ReportFragmentViewPagerSetup fragment = new ReportFragmentViewPagerSetup();
                Bundle b = new Bundle();
                b.putString("FROMDATE", startDate);
                b.putString("TODATE", endDate);
                fragment.setArguments(b);
                mFragmentManager.beginTransaction()
                        .replace(R.id.framecontainer, fragment).commit();
            }
        });

        lastquarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*date = appController.addDay(new Date(), -90);
                fromdate.setText(date);*/

                setCurrentDateOnView("3"); //set dates from todays to -90 days to edit text

                getDateValues();

                ReportFragmentViewPagerSetup fragment = new ReportFragmentViewPagerSetup();
                Bundle b = new Bundle();
                b.putString("FROMDATE", startDate);
                b.putString("TODATE", endDate);
                fragment.setArguments(b);
                mFragmentManager.beginTransaction()
                        .replace(R.id.framecontainer, fragment).commit();
            }
        });

        return rootview;
    }

    private void CallLastWeek() {

        fragment = new ReportFragmentViewPagerSetup();
        Bundle b = new Bundle();
        b.putString("FROMDATE", startDate);
        b.putString("TODATE", endDate);
        fragment.setArguments(b);
        mFragmentManager.beginTransaction()
                .replace(R.id.framecontainer, fragment).commit();
    }


    private void getDateValues() {

        startDate = fromdate.getText().toString().trim();
        endDate = todate.getText().toString().trim();

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");


        String reformattedStrDate = "";
        String reformatedToDate = "";
        try {

            startDate = myFormat.format(fromUser.parse(startDate));
            endDate = myFormat.format(fromUser.parse(endDate));
            Log.e("reformattedStr123", "" + startDate + "  ??/  " + endDate);

        } catch (ParseException e) {
            e.printStackTrace();
            appController.appendLog(appController.getDateTime() + " " + "/ " + "Add Patient" + e);
        }
    }

    private void setCurrentDateOnView(String val) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        todate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("-").append(month + 1).append("-")
                .append(day).append(" "));
        //came form last week
        if (val.equals("1")) {
            date = AppController.addDay(new Date(), -7);
            fromdate.setText(date);
        } else if (val.equals("2")) {  //came form last month
            date = appController.addDay(new Date(), -30);
            fromdate.setText(date);

        } else if (val.equals("3")) {      //came form last quarter
            date = appController.addDay(new Date(), -90);
            fromdate.setText(date);
        }
    }


    private void addListenerOnButton() {


        fromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });


        todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID1);

            }


        });

        lastweek.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    lastweek.setBackgroundColor(getResources().getColor(R.color.btn_back_sbmt));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    lastweek.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                return false;
            }

        });
        lastmonth.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    lastmonth.setBackgroundColor(getResources().getColor(R.color.btn_back_sbmt));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    lastmonth.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                return false;
            }

        });

        lastquarter.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    lastquarter.setBackgroundColor(getResources().getColor(R.color.btn_back_sbmt));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    lastquarter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                return false;
            }

        });

    }

    private void showDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                callDatePicker("1");

                break;

            case DATE_DIALOG_ID1:


                callDatePicker("2");

                break;
        }
    }

    private void callDatePicker(final String s) {
        final Calendar c2 = Calendar.getInstance();
        int mYear2 = c2.get(Calendar.YEAR);

        int mMonth2 = c2.get(Calendar.MONTH);
        int mDay2 = c2.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd1 = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (s.equals("1")) {

                            String date = dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year;

                          /*  fromdate.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);*/

                            fromdate.setText(year + "-" + (monthOfYear + 1) + "-"
                                    + dayOfMonth);
                        } else {
                            todate.setText(year + "-" + (monthOfYear + 1) + "-"
                                    + dayOfMonth);
                        }

                    }

                }, mYear2, mMonth2, mDay2);
        dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd1.show();

        //show age of pateint
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDetach() {
        super.onDetach();
        if (rootview != null) {
            rootview = null;
        }
        if (appController != null) {
            appController = null;
        }
        if (fragment != null) {
            fragment = null;
        }
        mListener = null;
        mFragmentManager = null;
        fromdate = null;
        todate = null;
        viewPager = null;
        tabLayout = null;
        filterDate = null;
        lastweek = null;
        lastmonth = null;
        lastquarter = null;
        startDate = null;
        endDate = null;

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.other_navigation, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }


}