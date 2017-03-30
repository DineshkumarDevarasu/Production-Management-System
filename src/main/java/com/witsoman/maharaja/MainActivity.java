package com.witsoman.maharaja;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.witsoman.maharaja.Util.HttpConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {
    /*custom calendar*/
    private static final String tag = "MyCalendarActivity";
    public String date_month_year;
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;

    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private Button gridcell;
    private String[] day_color;
    private List<CountsDto> countsDto;
    private List<CountsDto> countsDto1;
    private int listofCounts;

    private String startTimeStr;

    public int getListofCounts() {
        return listofCounts;
    }

    public void setListofCounts(int listofCounts) {
        this.listofCounts = listofCounts;
    }


    /*Layout reference*/
    LinearLayout myLinearLayout;
    RelativeLayout rlayoutchefname;
    LinearLayout customcalendaarlayout;
    LinearLayout orderDataListviewLayout;
    LinearLayout orderChefproductionView;


    /*View references*/
    View hline1;
    View hline2;
    TextView displayname;
    TextView tchefname;
    TextView tchefid;
    TextView displaydate;
    Button logout;
    String data;


    String ChefName;
    String STARTTIME, STOPTIME;

    /*save process server*/
    ChefProduction cp;

    public long UniqueId;
    public int OrderId;
    public String ChefId;
    public int ProductId;
    public String ProductName;
    public Date StartTime;
    public Date EndTime;
    public double Qty;
    public int VerifiedChef;
    public int CreatedUser;
    public int UpdatedUser;

    Button btnshowcalendar;
    LinearLayout buttonlayout,leftside;
    Button SelectedDayMonthYearButton;
    LinearLayout showdayslayout;
    RelativeLayout rlorderdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SimpleDateFormat dateObj = new SimpleDateFormat("dd-MM-yyyy");
        date_month_year = dateObj.format(new Date());
        tchefname = (TextView) findViewById(R.id.tvchefname);
        tchefid = (TextView) findViewById(R.id.tvchefid);


        myLinearLayout = (LinearLayout) findViewById(R.id.hometab);
        displayname = (TextView) findViewById(R.id.tvdpname);
        displaydate=(TextView)findViewById(R.id.tvdisplaydate);
        logout=(Button)findViewById(R.id.btnlogout);
       // orderDataLayout = (LinearLayout) findViewById(R.id.orderdatadetail);
        rlayoutchefname = (RelativeLayout) findViewById(R.id.rldisplaychefname);
       // rlayouttimemgnt = (RelativeLayout) findViewById(R.id.timemanagement);
       customcalendaarlayout = (LinearLayout) findViewById(R.id.customcalendarlayout);
        orderDataListviewLayout = (LinearLayout) findViewById(R.id.orderDataListLayout);
        orderChefproductionView = (LinearLayout) findViewById(R.id.orderChefproductionView);

        buttonlayout=(LinearLayout)findViewById(R.id.buttonlayout);
        SelectedDayMonthYearButton=(Button)findViewById(R.id.selectedDayMonthYear);
        showdayslayout=(LinearLayout)findViewById(R.id.showdayslayout);
        rlorderdata=(RelativeLayout)findViewById(R.id.rlorderdata);

        leftside=(LinearLayout)findViewById(R.id.leftside);


        buttonlayout.setVisibility(View.INVISIBLE);
        SelectedDayMonthYearButton.setVisibility(View.INVISIBLE);
        showdayslayout.setVisibility(View.INVISIBLE);
        rlorderdata.setVisibility(View.INVISIBLE);

        btnshowcalendar=(Button)findViewById(R.id.btnshowcalendar);
        btnshowcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonlayout.setVisibility(View.VISIBLE);
                SelectedDayMonthYearButton.setVisibility(View.VISIBLE);
                showdayslayout.setVisibility(View.VISIBLE);
                rlorderdata.setVisibility(View.VISIBLE);
                btnshowcalendar.setVisibility(View.INVISIBLE);

            }
        });

        hline1 = (View)findViewById(R.id.hline1);
        hline2 = (View)findViewById(R.id.hline2);

        hline1.setVisibility(View.INVISIBLE);
        hline2.setVisibility(View.INVISIBLE);
        rlayoutchefname.setVisibility(View.INVISIBLE);
        customcalendaarlayout.setVisibility(View.INVISIBLE);
        btnshowcalendar.setVisibility(View.INVISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hline1.setVisibility(View.INVISIBLE);
                hline2.setVisibility(View.INVISIBLE);
                rlayoutchefname.setVisibility(View.INVISIBLE);
                customcalendaarlayout.setVisibility(View.INVISIBLE);
                btnshowcalendar.setVisibility(View.INVISIBLE);
                orderChefproductionView.setVisibility(View.INVISIBLE);


            }
        });

        //Network connectivity
        NetworkConnection networkConnection = new NetworkConnection(MainActivity.this);
        if (networkConnection.isNetworkAvailable()) {
            new ChefNameAsyncTask().execute();

            new OrderDataAsyncTask().execute();

        } else {
            AlertDialogManager alert = new AlertDialogManager();
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return;
            //Toast.makeText(MainActivity.this,"No network connection",Toast.LENGTH_SHORT).show();
        }


        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);
        Log.e("yearmonth", "Year:" + year + "\t" + "Month: " + month);
        selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);
        new OrderAsyncTask().execute();

    }


    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            new OrderAsyncTask().execute();
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
            Log.e("premonth", "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);


        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            new OrderAsyncTask().execute();
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
            Log.e("nextmonth", "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
        }


    }


    @Override
    protected void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final int[] months = {1, 2, 3, 4, 5, 6, 7,8, 9, 10, 11, 12};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;

        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private int getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }


        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            int currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;
            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            boolean isRecordFound = false;
            for (int i = 1; i <= daysInMonth; i++) {
                // Log.d(currentMonthName, String.valueOf(i) + " "  + getMonthAsString(currentMonth) + " " + yy);

                isRecordFound = false;


                if (countsDto1 != null) {
                    for (CountsDto dtt : countsDto1) {

                        //check and pass date code here
                        if (dtt.getMonthNo() == String.valueOf(i)) {
                            list.add(String.valueOf(i) + "-RED" + "-"
                                    + getMonthAsString(currentMonth) + "-" + yy);
                            isRecordFound = true;
                        }
                    }

                }
                if (!isRecordFound) {
                    if (i == getCurrentDayOfMonth()) {
                        list.add(String.valueOf(i) + "-TDAY" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING
            try {

                Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
                day_color = list.get(position).split("-");
                String theday = day_color[0];
                String themonth = day_color[2];
                String theyear = day_color[3];
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                    if (eventsPerMonthMap.containsKey(theday)) {
                        num_events_per_day = (TextView) row
                                .findViewById(R.id.num_events_per_day);
                        Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                        num_events_per_day.setText(numEvents.toString());
                    }
                }
                // Set the Day GridCell
                gridcell.setText(theday);
                gridcell.setTag(theday + "-" + themonth + "-" + theyear);
                Log.d(tag, "Setting GridCell " + day_color[1] + "-" + theday + "-" + themonth + "-"
                        + theyear);

                if (day_color[1].equals("GREY")) {
                    gridcell.setTextColor(getResources()
                            .getColor(R.color.lightgray));
                } else if (day_color[1].equals("WHITE")) {
                    gridcell.setTextColor(getResources().getColor(
                            R.color.orrange));
                } else if (day_color[1].equals("RED")) {
                    gridcell.setBackgroundResource(R.drawable.badge);
                }
                if (day_color[1].equals("TDAY")) {
                    gridcell.setTextColor(getResources().getColor(R.color.blue));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            /*Refresh layout selected date before clear*/
            displaydate.setText("");

            new OrderDataAsyncTask().execute();
            date_month_year = (String) view.getTag();
            /*SimpleDateFormat dateObj = new SimpleDateFormat("dd-MM-yyyy");
            tdyDate = dateObj.format(new Date());*/
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);

            /*Refresh layout selected date*/
            myRefreshFunctionSelectedDate();
            new ChefOrderListAsyncTask().execute();
            Log.e("Selected date", date_month_year);

            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + parsedDate.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }




    private class OrderAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {
        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            StringBuilder sb;
            InputStream is = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Config.Order_URL + year + "/" + month);
                HttpResponse httpresponse = httpclient.execute(httpget);
                HttpEntity httpentitty = httpresponse.getEntity();
                is = httpentitty.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();


                countsDto1 = new ArrayList<CountsDto>();

                JSONArray jsonArray = new JSONArray(sb.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    CountsDto countsDto = new CountsDto();

                    JSONObject jb = (JSONObject) jsonArray.getJSONObject(i);

                    String date = jb.getString("Date") + month + "/" + year;

                    String theday = jb.getString("Date");
                    //  String themonth="September";
                    setListofCounts(Integer.parseInt(theday));
                    countsDto.setDate(date);
                    // countsDto.setMonth(themonth);
                    countsDto1.add(countsDto);

                }

                return sb.toString();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String order) {
            try {
                if (order != null) {
                    Log.e("orderdata", order);
                    countsDto1 = new ArrayList<CountsDto>();

                    MonthCountsDto monthsCountDto = new MonthCountsDto();
                    List<CountsDto> listOfCounts = new ArrayList<CountsDto>();
                    JSONArray jsonArray = new JSONArray(order);
                    final TextView[] myTextViewss = new TextView[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CountsDto countsDto = new CountsDto();
                        final TextView rowTextView = new TextView(MainActivity.this);
                        JSONObject jb = (JSONObject) jsonArray.getJSONObject(i);
                        // rowTextView.setText("Count:" + jb.getString("Count") + "\n" + "Date:" + jb.getString("Date") );
                        rowTextView.setId(i);
                        rowTextView.setPadding(10, 10, 0, 0);
                        // orderdata.setText(rowTextView.getText());
                        // orderDataLayout.addView(rowTextView);
                        myTextViewss[i] = rowTextView;
                        String date = jb.getString("Date") + "/" + month + "/" + year;


                        String theday = jb.getString("Date");
                        // String themonth="September";
                        setListofCounts(Integer.parseInt(theday));
                        listOfCounts.add(countsDto);
                        countsDto.setDate(date);
                        // countsDto.setMonth(themonth);
                        countsDto.setMonthNo(jb.getString("Date"));
                        countsDto1.add(countsDto);
                        monthsCountDto.setCountsDtoList(listOfCounts);
                    }
                    setCountsDto(listOfCounts);
                    if (listOfCounts.size() > 0) {
                        for (CountsDto countsDto : listOfCounts) {
                            System.out.println("Date:::" + countsDto.getDate() + "Size:;" + getCountsDto());
                        }
                    }
                }


                // Initialised
                adapter = new GridCellAdapter(getApplicationContext(),
                        R.id.calendar_day_gridcell, month, year);
                adapter.notifyDataSetChanged();
                calendarView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public int getCountsDto() {
        return countsDto.size();
    }

    public void setCountsDto(List<CountsDto> countsDto) {
        this.countsDto = countsDto;
    }


    private class ChefNameAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {
        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            StringBuilder sb;
            InputStream is = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Config.ChefName_URL);
                HttpResponse httpresponse = httpclient.execute(httpget);
                HttpEntity httpentitty = httpresponse.getEntity();
                is = httpentitty.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return sb.toString();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String chefname) {
            try {
                if (chefname != null) {

                    Log.e("chefname", chefname);

                    JSONArray jsonArray = new JSONArray(chefname);
                    final Button[] myButtonViews = new Button[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        final Button rowchefButton = new Button(MainActivity.this);


                        final TextView rowchefid = new TextView(MainActivity.this);

                        JSONObject jb = (JSONObject) jsonArray.getJSONObject(i);
                        // set some properties of rowTextView or something
                        rowchefButton.setText(jb.getString("ChefName").toUpperCase());
                        rowchefid.setText(jb.getString("ChefId"));
                        tchefname.setText(jb.getString("ChefName"));
                        tchefid.setText(jb.getString("ChefId"));
                        rowchefButton.setId(i);
                        rowchefButton.setTextColor(Color.WHITE);
                         rowchefButton.setBackgroundResource(R.drawable.chefbtnbg);
                      // rowchefButton.setBackgroundResource(R.drawable.chefname_rounded);



                        rowchefButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try{

                                    // orderChefproductionView.removeAllViewsInLayout();
                                /*Refresh layout selected date*/
                                    myRefreshFunctionSelectedDate();
                                    myRefreshFunctionCustomCalendar();
                                    if((orderChefproductionView).getChildCount() > 0)
                                        (orderChefproductionView).removeAllViews();
                                /*if((orderDataListviewLayout).getChildCount() > 0)
                                    (orderDataListviewLayout).removeAllViews();*/

                                /*all fields visible*/
                                    hline1.setVisibility(View.VISIBLE);
                                    hline2.setVisibility(View.VISIBLE);
                                    rlayoutchefname.setVisibility(View.VISIBLE);
                                    customcalendaarlayout.setVisibility(View.VISIBLE);
                                    orderChefproductionView.setVisibility(View.VISIBLE);
                                    /*show Calendar*/
                                    btnshowcalendar.setVisibility(View.VISIBLE);
                                    buttonlayout.setVisibility(View.INVISIBLE);
                                    SelectedDayMonthYearButton.setVisibility(View.INVISIBLE);
                                    showdayslayout.setVisibility(View.INVISIBLE);
                                    rlorderdata.setVisibility(View.INVISIBLE);

                                    //orderChefproductionView.invalidate();
                                    displayname.setText("WELCOME :" + "\t" + rowchefButton.getText());
                                    selectedDayMonthYearButton.clearFocus();
                                    displaydate.setText("Selected Date"+"\t"+date_month_year);

                                    displayname.setTextColor(Color.BLACK);
                                    ChefName = displayname.getText().toString();
                                    tchefname.setText(rowchefButton.getText());

                                    tchefid.setText(rowchefid.getText());
                                    ChefName = tchefname.getText().toString();
                                    ChefId = tchefid.getText().toString();
                                    new ChefOrderListAsyncTask().execute();


                                    Log.e("choosechefname", ChefName);
                                    Log.e("choosechefid", ChefId);

                                }catch (NullPointerException e){e.printStackTrace();}


                            }
                        });
                        myLinearLayout.addView(rowchefButton);
                        // save a reference to the textview for later
                        myButtonViews[i] = rowchefButton;



                    }

                }

            } catch (Exception e) {e.printStackTrace();  }


        }
    }


    private class OrderDataAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            StringBuilder sb = null;
            InputStream is = null;
            try {
                System.out.println("Date ::" + Config.Order_URL + date_month_year);
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Config.Order_URL + date_month_year);
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                sb = new StringBuilder();
                String line = null;
                while ((line =
                        reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return sb.toString();

            } catch (Exception e) {
                Log.e("Exception", e.toString());
                return null;
            }
        }


        protected void onPostExecute(final String order) {
            try {


               // Log.e("Response", order);

                if (order != null) {
                    orderDataListviewLayout.removeAllViewsInLayout();

                   /* if((orderDataListviewLayout).getChildCount() > 0)
                        (orderDataListviewLayout).removeAllViews();*/
                    JSONArray jsonArray = new JSONArray(order);
                    final TextView[] myTextViewss = new TextView[jsonArray.length()];
                    final Button[] myStartButtons = new Button[jsonArray.length()];
                    final TextView[] myStartTimetv = new TextView[jsonArray.length()];
                    final TextView[] mychooseorder = new TextView[jsonArray.length()];


                    for (int i = 0; i < jsonArray.length(); i++) {
                        final TextView orderrow = new TextView(MainActivity.this);
                        final Button startButton = new Button(MainActivity.this);
                        final TextView tvchooseorder = new TextView(MainActivity.this);
                        final TextView tvstarttime = new TextView(MainActivity.this);
                        final TextView roworderid = new TextView(MainActivity.this);
                        final TextView rowproductid = new TextView(MainActivity.this);
                        final TextView rowproductname = new TextView(MainActivity.this);
                        final TextView rowqty = new TextView(MainActivity.this);
                        final TextView rowcreateduser = new TextView(MainActivity.this);
                        final TextView rowupdateduser = new TextView(MainActivity.this);
                        final JSONObject jb = (JSONObject) jsonArray.getJSONObject(i);

                        orderrow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tvstarttime.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        startButton.setLayoutParams(new ViewGroup.LayoutParams(250, 60));
                        startButton.setBackgroundResource(R.drawable.start);
                       /* startButton.setBackgroundResource(R.drawable.startbutton_rounded);
                        startButton.setText("Start");
                        startButton.setTextColor(Color.BLACK);*/


                        roworderid.setText(jb.getString("OrderId"));
                        rowproductid.setText(jb.getString("ProductId"));
                        rowproductname.setText(jb.getString("ProductName"));
                        rowqty.setText(jb.getString("Qty"));
                        rowcreateduser.setText(jb.getString("CreatedUser"));
                        rowupdateduser.setText(jb.getString("UpdatedUser"));
                        startButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ChefName = displayname.getText().toString().trim();
                                if ( ChefName.length() <= 0){
                                    Toast.makeText(getApplicationContext(),"Select Chefname name is required!",Toast.LENGTH_LONG).show();


                                } else {

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    STARTTIME = df.format(c.getTime());
                                    try {
                                        StartTime = df.parse(STARTTIME);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    startButton.setVisibility(View.INVISIBLE);
                                    Log.e("StartTime",""+StartTime);

                                   /* hline1.setVisibility(View.INVISIBLE);
                                    hline2.setVisibility(View.INVISIBLE);
                                    rlayoutchefname.setVisibility(View.INVISIBLE);
                                    customcalendaarlayout.setVisibility(View.INVISIBLE);
                                    orderChefproductionView.setVisibility(View.INVISIBLE);
                                    startButton.setVisibility(View.INVISIBLE);
                                    orderrow.setVisibility(View.INVISIBLE);*/
                                    tvstarttime.setText("Start Time :" + "\t" + STARTTIME);
                                    Log.e("StartTime", "" + STARTTIME);


                                    try {

                                        setStartTimeStr(STARTTIME);
                                       /*set data for server*/
                                        cp = new ChefProduction();
                                        cp.setOrderId(Integer.parseInt(jb.getString("OrderId")));
                                        cp.setChefId(Integer.parseInt(ChefId));
                                        cp.setProductId(Integer.parseInt(String.valueOf(rowproductid.getText())));
                                        cp.setStartTime(StartTime);
                                         cp.setVerifiedChef(1);
                                         data = new Gson().toJson(cp);
                                        new StartDataAsyncTask().execute();

                                        //Refresh caledar
                                        myRefreshFunctionCustomCalendar();

                                    } catch (Exception e) {
                                        Log.e("Save", "");
                                    }


                                    if(orderChefproductionView != null)
                                    {
                                       new ChefOrderListAsyncTask().execute();
                                    }


                                }
                            }
                        });

                        // set some properties of rowTextView or something
                        orderrow.setText(jb.getString("ProductName")+"="+ jb.getString("Qty")+"Kg");
                        Log.e("order", "Product Name:" + jb.getString("ProductName")+"\t"+"Quantity:" + jb.getString("Qty"));
                        orderrow.setId(i);

                        orderrow.setPadding(10, 10, 10, 10);
                        // add the textview to the linearlayout  each day full  event

                        orderDataListviewLayout.addView(orderrow);
                        orderDataListviewLayout.addView(startButton);
                        orderDataListviewLayout.addView(tvstarttime);


                        // save a reference to the textview for later
                        myTextViewss[i] = orderrow;
                        myStartButtons[i] = startButton;
                        myStartTimetv[i] = tvstarttime;
                        mychooseorder[i] = tvchooseorder;


                        orderrow.setTextColor(Color.WHITE);
                        if (i % 2 == 1) {
                            // Set the background color for alternate row/item
                            orderrow.setBackgroundColor(Color.parseColor("#3990CA"));
                        } else {
                            // Set the background color for alternate row/item
                            orderrow.setBackgroundColor(Color.parseColor("#3990CA"));
                        }
                    }
                } else {
                    orderDataListviewLayout.removeAllViewsInLayout();

                       final TextView textView = new TextView(MainActivity.this);
                        textView.setText("No Records");
                        orderDataListviewLayout.addView(textView);



                }


            } catch (Exception e) {
                Log.e("order", e.toString());
            }

        }


    }


    private class StopDataAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            HttpConfig hc = new HttpConfig();
            result = hc.doPost(data, Config.SaveDataStop_URL);
            Log.e("SaveData", data);
            System.out.println("Result Install " + result);
            result = result.replaceAll("\r", "");

            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            //progressDialog.dismiss();
            String msg = null, mHint = null;

            if (result != null && result.length() > 0) {
                Log.e("resultsave", result);
                JSONObject obj1 = null;
                try {
                    System.out.println("Result " + result);
                    obj1 = new JSONObject(result);
                    msg = obj1.getString("message");
                    if (msg.equalsIgnoreCase("Success")) {


                    } else if (msg.equalsIgnoreCase("Failure")) {


                    } else {

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }
    }


    private class StartDataAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            HttpConfig hc = new HttpConfig();
            result = hc.doPost(data, Config.SaveDataStart_URL);
            Log.e("SaveData", data);
            System.out.println("Result Install " + result);
            result = result.replaceAll("\r", "");
            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            //progressDialog.dismiss();
            String msg = null, mHint = null;

            if (result != null && result.length() > 0) {
                Log.e("resultsave", result);
                JSONObject obj1 = null;
                try {
                    System.out.println("Result " + result);
                    obj1 = new JSONObject(result);
                    msg = obj1.getString("message");
                    if (msg.equalsIgnoreCase("Success")) {


                    } else if (msg.equalsIgnoreCase("Failure")) {


                    } else {

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit this application?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Exit");
        alert.setIcon(R.drawable.alertlogo);
        alert.show();

    }


    private class  ChefOrderListAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            StringBuilder sb = null;
            InputStream is = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Config.ChefOrderList_URL +ChefId+"/"+ date_month_year);
                Log.e("ChefOrderUrlList",Config.ChefOrderList_URL+ChefId+"/"+date_month_year);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                sb = new StringBuilder();
                String line = null;
                while ((line =
                        reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                return sb.toString();

            } catch (Exception e) {
                Log.e("ChefOrderListException", e.toString());
                return null;
            }
        }


        protected void onPostExecute(final String cheforder) {
            try {
               // orderChefproductionView.removeAllViewsInLayout();
                if((orderChefproductionView).getChildCount() > 0)
                    (orderChefproductionView).removeAllViews();
                Log.e("ChefOrderResponse", cheforder);
                if (cheforder != null) {

                    /*Selected date show on calendar*/
                    myRefreshFunctionSelectedDate();

                    JSONArray jsonArray = new JSONArray(cheforder);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        final TextView cheforderrow = new TextView(MainActivity.this);

                        final EditText etquantity=new EditText(MainActivity.this);
                        final Button stopButton = new Button(MainActivity.this);
                        final TextView tvstarttime = new TextView(MainActivity.this);
                        final TextView tvstoptime = new TextView(MainActivity.this);
                        final TextView tvqty=new TextView(MainActivity.this);
                        final TextView roworderid = new TextView(MainActivity.this);
                        final TextView rowproductid = new TextView(MainActivity.this);
                        final TextView rowproductname = new TextView(MainActivity.this);
                        final TextView rowqty = new TextView(MainActivity.this);
                        final TextView rowcreateduser = new TextView(MainActivity.this);
                        final TextView rowupdateduser = new TextView(MainActivity.this);
                        final TextView rowUniqueId=new TextView(MainActivity.this);


                        final JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                        cheforderrow.setText(jsonObject.getString("ProductName")+"="+jsonObject.getString("Qty")+"Kg");
                        orderChefproductionView.addView(cheforderrow);
                        orderChefproductionView.addView(etquantity);
                        orderChefproductionView.addView(stopButton);
                        orderChefproductionView.addView(tvqty);
                        orderChefproductionView.addView(tvstarttime);
                        orderChefproductionView.addView(tvstoptime);
                        roworderid.setText(jsonObject.getString("OrderId"));
                        rowproductid.setText(jsonObject.getString("ProductId"));
                        rowproductname.setText(jsonObject.getString("ProductName"));
                        rowqty.setText(jsonObject.getString("Qty"));
                        rowcreateduser.setText(jsonObject.getString("CreatedUser"));
                        rowupdateduser.setText(jsonObject.getString("UpdatedUser"));
                        rowUniqueId.setText(jsonObject.getString("UniqueId"));


                        String sss ="";
                        sss = " orderid: " + jsonObject.getString("OrderId");

                        cheforderrow.setPadding(10,10,10,10);
                        cheforderrow.setTextColor(Color.BLACK);
                        if (i % 2 == 1) {
                            // Set the background color for alternate row/item
                            cheforderrow.setBackgroundColor(getResources().getColor(R.color.seablue));
                        } else {
                            cheforderrow.setBackgroundColor(Color.parseColor("#8D05D3F2"));
                        }

                        cheforderrow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        stopButton.setBackgroundResource(R.drawable.stop);
                        stopButton.setLayoutParams(new LinearLayout.LayoutParams(250,60));
                        etquantity.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        etquantity.setSingleLine(true);
                        etquantity.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        etquantity.setTextColor(Color.BLACK);
                        etquantity.setBackgroundResource(R.drawable.quantity_rounded);
                        etquantity.setHint("Enter Quantity");



                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                // SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                                STOPTIME = df.format(c.getTime());
                                try {
                                    EndTime = df.parse(STOPTIME);
                                    Log.e("Endtime",""+EndTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                /*validation quantity edittext*/
                                String Qty=etquantity.getText().toString().trim();
                                if (Qty.length() <= 0) {
                                    etquantity.setError("Fill the Field Quantity");
                                    etquantity.requestFocus();

                                }else {

                                   /* hline1.setVisibility(View.INVISIBLE);
                                    hline2.setVisibility(View.INVISIBLE);
                                    rlayoutchefname.setVisibility(View.INVISIBLE);
                                    customcalendaarlayout.setVisibility(View.INVISIBLE);
                                    orderChefproductionView.setVisibility(View.INVISIBLE);*/


                                    etquantity.setVisibility(View.INVISIBLE);
                                    tvqty.setText("Qty:"+"\t"+Qty+"kg");
                                    tvstarttime.setText("Start Time :" + "\t" + getStartTimeStr());
                                    tvstoptime.setText("End Time :" + "\t" + STOPTIME);
                                    stopButton.setVisibility(View.INVISIBLE);
                                    try {
                                        /*set data for server*/
                                        cp = new ChefProduction();
                                        cp.setOrderId(Integer.parseInt(String.valueOf(roworderid.getText())));
                                        cp.setUniqueId(Integer.parseInt(String.valueOf(rowUniqueId.getText())));
                                        cp.setProductId(Integer.parseInt(String.valueOf(rowproductid.getText())));
                                        cp.setEndTime(EndTime);
                                        cp.setQty(Double.parseDouble(Qty));
                                        data = new Gson().toJson(cp);
                                        new StopDataAsyncTask().execute();
                                    } catch (Exception e) {
                                        Log.e("Save", "");
                                    }
                                }


                            }
                        });



                        }

                } else {


                }


            } catch (Exception e) {
                Log.e("Cheforder", e.toString());
            }

        }


    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }



    private void  myRefreshFunctionSelectedDate() {
            try{
                if(rlayoutchefname != null){
                    displaydate.setText("");
                    displaydate=new TextView(this);
                    displaydate.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    displaydate.setTextColor(Color.BLACK);
                    displaydate.setText("Selected Date"+"\t"+date_month_year);
                    rlayoutchefname.addView(displaydate);
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }


    }


    private void myRefreshFunctionCustomCalendar(){

        try{
            displaydate.setText("");
            SimpleDateFormat dateObj = new SimpleDateFormat("dd-MM-yyyy");
            date_month_year = dateObj.format(new Date());
            new OrderDataAsyncTask().execute();
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);
                    /*Refresh layout selected date*/
            new ChefOrderListAsyncTask().execute();
            Log.e("Selected date-->", date_month_year);
        }catch (NullPointerException e){e.printStackTrace();}





    }

}