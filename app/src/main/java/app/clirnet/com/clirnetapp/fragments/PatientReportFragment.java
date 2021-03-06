package app.clirnet.com.clirnetapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import app.clirnet.com.clirnetapp.R;
import app.clirnet.com.clirnetapp.adapters.ChartDataAdapter;
import app.clirnet.com.clirnetapp.app.AppController;
import app.clirnet.com.clirnetapp.helper.SQLController;
import app.clirnet.com.clirnetapp.models.Counts;
import app.clirnet.com.clirnetapp.reports.ChartItem;
import app.clirnet.com.clirnetapp.reports.LineChartItem;

import static android.content.Context.MODE_PRIVATE;

//we are calling two seprate fragments for each report ie for daily patient count and age wise report from this fragment class
public class PatientReportFragment extends Fragment {


    private ArrayList<String> dateList;
    private ArrayList<String> nocountsperday;

    private OnFragmentInteractionListener mListener;
    private View view;

    private AppController appController;
    private String fromDate, toDate;

    private LinkedList<String> newdate;
    private SQLController sqlController;

    public PatientReportFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromDate = bundle.getString("FROMDATE");
            toDate = bundle.getString("TODATE");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        AppController.getInstance().trackScreenView("Patient Report Fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        view = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_patient_update, container, false);


        appController = new AppController();

        this.setValuesSharedPrefrence("one");

        ListView lv = (ListView) view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();


        list.add(new LineChartItem(generateDataLine(), getContext(), fromDate, toDate));

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);

        BarChartFragment fragment2 = new BarChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("FROMDATE", fromDate);
        bundle.putString("TODATE", toDate);
        fragment2.setArguments(bundle);

        FragmentManager fragmentManager2 = getChildFragmentManager();
        fragmentManager2.beginTransaction().replace(R.id.flContent1, fragment2).commit();

        return view;
    }


    private ChartData<?> generateDataLine() {

        if (appController == null) {
            appController = new AppController();
        }

        ArrayList<Entry> values = new ArrayList<>();
        try {

            if (sqlController == null) {
                sqlController = new SQLController(getContext().getApplicationContext());
                sqlController.open();
            }

            ArrayList<Counts> countsNo;
            dateList = new ArrayList<>();
            nocountsperday = new ArrayList<>();

            countsNo = sqlController.countPerDay(fromDate, toDate);


            int size = countsNo.size();

            if (size > 0) {
                for (int im = 0; im < size; im++) {
                    String strCount = countsNo.get(im).getCount();
                    String strDate = countsNo.get(im).getDate().trim();

                    nocountsperday.add(strCount);
                    dateList.add(strDate);

                }
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            newdate = new LinkedList<>();
            List<String> newcount = new LinkedList<>();
            try {
                Date stdate = format.parse(fromDate);
                Date eddate = format.parse(toDate);

                String newsd = format1.format(stdate);
                String newed = format1.format(eddate);

                Date d1 = format1.parse(newsd);
                Date d2 = format1.parse(newed);


                Calendar start = Calendar.getInstance();
                start.setTime(d1);
                Calendar end = Calendar.getInstance();
                end.setTime(d2);
                end.add(Calendar.DATE, 1);

                //here we are checking if date is present in dateList or not which came from db if not we r adding customly date and count which is 0. 29/11-2016 By.Ashish
                for (Date date1 = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date1 = start.getTime()) {

                    Format formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.ENGLISH);
                    Date date;
                    String s = formatter.format(date1);

                    if (dateList.contains(s)) {
                        int getindex = dateList.indexOf(s);
                        String countval = nocountsperday.get(getindex);

                        date = sdf.parse(s);

                        String convertedDate = sdf.format(date);
                        newdate.add(convertedDate);
                        newcount.add(countval);

                    } else {
                        date = sdf.parse(s);

                        String convertedDate = sdf.format(date);
                        newdate.add(convertedDate);
                        newcount.add("0");
                    }
                }
            } catch (ParseException e) {

                e.printStackTrace();
                appController.appendLog(appController.getDateTime() + " " + "/ " + "PatientReportFragment" + e + " Line Number: " + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }

            int size1 = newcount.size();
            if (size1 > 0) {
                for (int in = 0; in < size1; in++) {
                    Float no = Float.valueOf(newcount.get(in));
                    values.add(new Entry(no, in));
                    //  values.add(new Entry(no,in));
                }
            } else {
                Toast.makeText(getContext(), "No Data To Display.", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            new AppController().appendLog(new AppController().getDateTime() + " " + "/ " + "PatientReportFragment" + e + " Line Number: " + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(values, "Day Wise Count");

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
            set1.setFillDrawable(drawable);

        } else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(newdate, dataSets);

        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(16f);
        return data;


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
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
        if (view != null) {
            view = null;
        }
        if (appController != null) {
            appController = null;
        }
        if(sqlController!=null){
            sqlController=null;
        }
        fromDate = null;
        toDate = null;
        dateList = null;
        newdate = null;
        nocountsperday = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setValuesSharedPrefrence(String key) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppController.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("value", key);
        // editor.putInt("idName", 12);
        editor.apply();

    }
}
