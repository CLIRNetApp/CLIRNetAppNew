package app.clirnet.com.clirnetapp.app;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.clirnet.com.clirnetapp.R;
import app.clirnet.com.clirnetapp.helper.ClirNetAppException;
import app.clirnet.com.clirnetapp.helper.SQLController;
import app.clirnet.com.clirnetapp.helper.SQLiteHandler;

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();

    public static final String PREFS_NAME = "savedViewValue";
    private static final String PREFS_NAMEsavedCredit = "savedCredit";
    private static final String FISRT_TIME_LOGIN = "firstTimeLogin";

    private RequestQueue mRequestQueue;
    Request.Priority priority = Request.Priority.HIGH;
    private int hour;
    private int minute;


    private static AppController mInstance;
    private TextView fromtime;
    private HashMap<String, String> listBannerInformation;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        Log.d("Adding request to ", "" + req.getUrl());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public Request.Priority getPriority() {
        return priority;
    }

    public void setPriority(Request.Priority p) {
        priority = p;
    }

    public void appendLog(String text) {

        File logFile = new File("sdcard/PatientsImages/log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Remove leading zero from the age filed if any
    public static String removeLeadingZeroes(String value) throws ClirNetAppException {
        String val = null;
        if (value.trim() != null) {
            try {
                val = new Integer(value).toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClirNetAppException("Remove leading zeros");

            }
        }

        return val;
    }


    //this will gives you a age from the date
    public int getAge(int year, int monthOfYear, int dayOfMonth) {
        Date now = new Date();
        int nowMonths = now.getMonth() + 1;
        int nowDate = now.getDate();
        int nowYear = now.getYear() + 1900;
        int result = nowYear - year;

        if (monthOfYear > nowMonths) {
            result = 0;
        } else if (dayOfMonth == nowMonths) {
            if (dayOfMonth > nowDate) {
                result = 0;
            }

        } // this will caklculate months if year <0
        /*else {

			result=nowMonths-monthOfYear;

		}*/

        return result;
    }

    public String getDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        return (sdf.format(cal.getTime()));
    }

    public String getDateTimenew() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return (sdf.format(cal.getTime()));
        //return "22-12-2016 04:05:07"; //for test
    }

    //This will remove commas from string

    public String removeCommaOccurance(String str) {
        //String str = ",,,,,,,,,,kushal,,,,,,,,,,,,,,,,,,mayurv,narendra,dhrumil,mark, ,,,, ";
        String splitted[] = str.split(",");
        StringBuilder sb = new StringBuilder();
        String retrieveData = "";
        for (int i = 0; i < splitted.length; i++) {
            retrieveData = splitted[i];
            if ((retrieveData.trim()).length() > 0) {

                if (i != 0) {
                    sb.append(",");
                }
                sb.append(retrieveData);

            }
        }

        str = sb.toString();
        str = str.startsWith(",") ? str.substring(1) : str; //this will remove , from start of the filtered string after removing middle commas from string
        //System.out.println(str);
        return str;

    }

    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public static String addDay(Date date, int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);

        String dateis = sdf.format(cal.getTime());
        return dateis;
    }

    public static Date addDay1(Date date, int i) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    //method to add days to current month
    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }


    //check if the ailment string contains all the nubers or not
    public boolean findNumbersAilment(String value) {
        String result = value.replaceAll("[,]", "");
        String regex = "[0-9]+";
        return result.matches(regex);

    }

    public boolean findEmptyString(String value) {
        boolean result = (TextUtils.isEmpty(value.trim()));
        return result;

    }
    //check the input filed has any text or not
    //return true if it contains text otherwise return false.

    //how to call this function ex.Appcontroller.hasContain(EditText);
    //if(!AppController.hasText(edittext))ret=false;
    public static boolean hasContain(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setText("put some msg here");
            return false;
        }
        return true;
    }

    public Integer findLength(String value) {
        int result = (value.length());

        return result;
    }

    public String ConvertDateFormat(String date) {
        SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

        String flag = "0";
        String frmtedDate = "";

        //convert visit date from 2016-11-1 to 2016-11-01
        try {

            frmtedDate = myFormat.format(fromUser.parse(date));
            Log.e("reformattedStrqq", "" + frmtedDate);

        } catch (ParseException e) {
            e.printStackTrace();
            this.appendLog(this.getDateTime() + " " + "/ " + "App Controller" + e+" "+Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
        return frmtedDate;
    }

    public boolean emptyDataValidation(String value) {
        if (value.trim().equals("") || value.equals(null)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean PhoneNumberValidation(String value) {
        boolean result = true;
        try {
            long val = Integer.parseInt(value.trim());
            if (val < 1000000000) {
                result = false;
            } else {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // to do remove it and write update while inserting
    //Method to validate if there is allready records in db to check
    public boolean isDuplicate(List<String> col, String value) {
        boolean isDuplicate = false;
        for (String s : col) {
            if (s.equals(value)) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }

    //it will remove : from string if it contains 12-13-2016
    public String removeColunChar(String val) {
        String result = null;

        if (val.length() > 0) {
            result = val.replaceAll("[-+.^:,]", "");
        }
        return result;
    }

    private long getFileSize(String strfile) {
        long length = 0;

        try {

            File file = new File(strfile);
            length = file.length();
            length = length / 1024;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return length;
    }

    //get the hours between two dates
    public static int hoursAgo(String datetime) {
        Date date = null; // Parse into Date object
        long differenceInHours = 0;
        try {
            if (datetime != null) {
                date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(datetime);
                Date now = Calendar.getInstance().getTime(); // Get time now
                long differenceInMillis = now.getTime() - date.getTime();
                differenceInHours = (differenceInMillis) / 1000L / 60L / 60L; // Divide by millis/sec, secs/min, mins/hr
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) differenceInHours;
    }


    public void showAdDialog(final Context context, final String img_name) {

        final Context context1 = context;

        String manufactured_by = null;
        String marketed_by = null;
        String clinical_trial_link=null;
        String link_to_page= null;
        String product_image_name=null;
        String product_image_url=null;

        SQLController sqlController = new SQLController(context);
        try {
            sqlController.open();
            try {
                HashMap<String, String> list = sqlController.retrieveMarketed_byandMmanufactured_by(img_name);

                manufactured_by = list.get("manufactured_by");
                marketed_by = list.get("marketed_by");
                clinical_trial_link=list.get("clinical_trial_link");
                link_to_page=list.get("link_to_page");
                product_image_name=list.get("product_image_name");
                product_image_url=list.get("product_image2");
                Log.e("Banner Info ", " Banner Info   " + list.size() + " " + manufactured_by + "   " + marketed_by+" product_image_name "+product_image_name);
                listBannerInformation = sqlController.getBannerInformation(img_name);
                String brand_name = listBannerInformation.get("brand_name");
                String generic_name = listBannerInformation.get("generic_name");
                Log.e("brand_namegeneric_name ", " Banner Info   " + listBannerInformation.size() + " " + brand_name + "   " + generic_name);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.ad_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Banner Info");

        TextView mktcmpny_name = (TextView) dialog.findViewById(R.id.mktcmpny_name);
        TextView maketed_by = (TextView) dialog.findViewById(R.id.produced_by);
        ImageView productImage=(ImageView)dialog.findViewById(R.id.productImage);

        if(!product_image_name.equals("")&& product_image_url != null && product_image_url.trim().length()>0 && product_image_name != null && product_image_name.trim().length()>0){

            BitmapDrawable d = new BitmapDrawable(context.getResources(), "sdcard/BannerImages/" + product_image_name + ".png"); // path is ur resultant //image

            Log.e("BitmapDrawable", "" + d +" ////  "+product_image_name);
            if(d!=null){
            productImage.setImageDrawable(d);}
        }else{
            productImage.setImageResource(R.drawable.brand);
        }
        if (manufactured_by != null && manufactured_by.length() > 0) {

            mktcmpny_name.setText(manufactured_by);
        }

        if (marketed_by != null && marketed_by.length() > 0) {

            maketed_by.setText(marketed_by);
        }

        //  dialog.setCancelable(false);
        Button close = (Button) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button call_me = (Button) dialog.findViewById(R.id.call_me);

        call_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creteCallMeDialog(context1, "call_me");
            }
        });

        Button meet_me = (Button) dialog.findViewById(R.id.meet_me);
        meet_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creteCallMeDialog(context1, "meet_me");
            }
        });

        Button btnseedrugProfile = (Button) dialog.findViewById(R.id.seedrugProfile);
        final String finalLink_to_page = link_to_page;

        btnseedrugProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(finalLink_to_page != null && finalLink_to_page.trim().length()>0){
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink_to_page)));
                   /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink_to_page));
                    startActivity(browserIntent);*/
                }else{
                    Toast.makeText(context1,"No Link Avaialable",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnseeclinical_trial=(Button)dialog.findViewById(R.id.seeclinical_trial);
        final String finalClinical_trial_link = clinical_trial_link;
        btnseeclinical_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalClinical_trial_link != null && finalClinical_trial_link.trim().length()>0 ){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalClinical_trial_link));
                    context.startActivity(browserIntent);
                }else{
                    Toast.makeText(context1,"No Link Avaialable",Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    private void creteCallMeDialog(final Context context1, final String called_from) {

        final Dialog dialog = new Dialog(context1);
        dialog.setContentView(R.layout.callme_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Request Sample Form");
        final TextView date = (TextView) dialog.findViewById(R.id.date);
        final TextView fromtime = (TextView) dialog.findViewById(R.id.fromtime);

        final TextView totime = (TextView) dialog.findViewById(R.id.totime);

        final EditText location = (EditText) dialog.findViewById(R.id.location);
        final EditText reason = (EditText) dialog.findViewById(R.id.reason);


        final SQLiteHandler dbController = new  SQLiteHandler(context1);;
        try {
            SQLController sqlController = new SQLController(context1);
            sqlController.open();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button send = (Button) dialog.findViewById(R.id.send);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context1, date);
            }
        });

        fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callTimePicker(context1, fromtime);

            }
        });

        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callTimePicker(context1, totime);//totime we are passing the text view refrence to call picker so he can set value to that view

            }
        });
        getCurrentTime(fromtime);
        getCurrentTime(totime);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = location.getText().toString();
                String strreason = reason.getText().toString();
                String strDate = date.getText().toString();
                String brand_name = null, generic_name = null;
                String company_id = null, doctor_id = null, doc_mem_id = null, modified_on = null, is_deleted = null;
                String deleted_on = null, is_disabled = null, disabled_by = null, disabled_on = null;
                String request_on= getDateTimenew();//get current time stamp

                if (strDate.length() <= 0) {
                    date.setError("Please Enter Date");
                    return;
                }
                if (address != null && address.trim().length() > 0) {
                    location.setError("");

                } else {
                    location.setError("Please Enter Value");
                    return;
                }

                if (strreason != null && strreason.trim().length() > 0) {
                    reason.setError("");
                } else {
                    reason.setError("Please Enter Value");
                    return;
                }
                String from_time=fromtime.getText().toString();
                String to_time=totime.getText().toString();
                String reqest_fullfilled=null;

                String modified_by=null;
                String flag="0";
                if (listBannerInformation.size() > 0) {

                    brand_name = listBannerInformation.get("brand_name");
                    generic_name = listBannerInformation.get("generic_name");
                    company_id = listBannerInformation.get("company_id");
                    doctor_id = listBannerInformation.get("doctor_id");
                    doc_mem_id = listBannerInformation.get("doc_mem_id");

                }
                String added_by=doctor_id;
                String added_on=request_on;
                //TODO TOMMOROW add data into db
                dbController.addCallMeetMeData(brand_name,company_id,generic_name,called_from,strDate,from_time,to_time,
                  address,strreason,doctor_id,doc_mem_id,request_on,reqest_fullfilled,modified_on,modified_by,is_deleted,is_disabled,disabled_by,disabled_on,deleted_on,flag,added_on,added_by);
                Toast.makeText(context1,"Message send  you will recieve responce soon",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callTimePicker(Context context1, final TextView textView) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context1, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // textView.setText( selectedHour + ":" + selectedMinute);
                updateTime(selectedHour, selectedMinute, textView);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void getCurrentTime(TextView fromtime) {
        final Calendar c = Calendar.getInstance();
        int hour;
        int minute;
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute, fromtime);

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins, TextView fromtime) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        fromtime.setText(aTime);
    }

    private void showDatePicker(Context context, final TextView date) {
        final Calendar c2 = Calendar.getInstance();
        int mYear2 = c2.get(Calendar.YEAR);

        int mMonth2 = c2.get(Calendar.MONTH);
        int mDay2 = c2.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd1 = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }

                }, mYear2, mMonth2, mDay2);
        Date newDate = c2.getTime();
        dpd1.getDatePicker().setMinDate(newDate.getTime());
        //  dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd1.show();
        //show age of pateint
    }

    public void saveBannerDataIntoDb(String image_url, Context context, String doctor_membership_number, String action) {
        SQLiteHandler dbController = null;

        String docId = null;
        String company_id = null;
        String banner_id = null;
        String banner_folder = null;
        String banner_image = image_url;
        int banner_type_id = 0;
        String module = "patient";
        String is_deleted = null;
        String is_disbled = null;
        String display_time = getDateTimenew();
        try {
            SQLController sqlController = new SQLController(context);
            sqlController.open();
            dbController = new SQLiteHandler(context);
            docId = sqlController.getDoctorId();
            company_id = sqlController.getCompany_id();
            Log.e("company_id", "  " + company_id);
        } catch (ClirNetAppException | SQLException e) {
            e.printStackTrace();
            appendLog(getDateTime() + " " + "/ " + "App Controller" + e+" "+Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
        try {
            if(dbController !=null)
            if (action.equals("display")) {


                dbController.addBannerDisplayData(docId, doctor_membership_number, company_id, banner_id, banner_folder, banner_image, banner_type_id, module, is_deleted, is_disbled, display_time);

            } else {

                dbController.addBannerClickedData(docId, doctor_membership_number, company_id, banner_id, banner_folder, banner_image, banner_type_id, module, is_deleted, is_disbled, display_time);

            }
        }catch (NullPointerException e){
            e.printStackTrace();
            this.appendLog(getDateTime()+"saveBannerDataIntoDb"+e);
        }

    }
    public boolean getFirstTimeLoginStatus(){
        SharedPreferences pref = getSharedPreferences(PREFS_NAMEsavedCredit, Context.MODE_PRIVATE);
        String firstTimeLogin = pref.getString(FISRT_TIME_LOGIN
                , null);
        Log.e("firstTimeLogin", ""+ firstTimeLogin);
        if(firstTimeLogin == null){
            return false;
        }else if(firstTimeLogin.equals("false")){

            return false;
        }
        return  true;
    }
}

