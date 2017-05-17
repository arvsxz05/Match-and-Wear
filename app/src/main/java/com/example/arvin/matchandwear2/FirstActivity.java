package com.example.arvin.matchandwear2;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arvin.matchandwear2.data.ClothesContract;
import com.example.arvin.matchandwear2.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.arvin.matchandwear2.R.id.calendar_day_gridcell;

public class FirstActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static Context mContext;

    static ImageView imageTop;
    static Button uploadTop;
    static ImageView imageBottom;
    static Button uploadBottom;
    static ImageView imageFootwear;
    static Button uploadFootwear;
    static Spinner mOutfitTypeSpinner;

    static Button takePicTop;
    static Button takePicBottom;
    static Button takePicFootwear;

    static EditText outfitName;
    static EditText dateText;

    static Button saveOutfit;

    static Uri newOutfitUri;

    static Integer mOutfit = ClothesContract.OutfitEntry.TYPE_CASUAL;

    private static final int PICK_IMAGE_TOP = 100;
    private static final int PICK_IMAGE_BOTTOM = 200;
    private static final int PICK_IMAGE_FOOTWEAR = 300;
    private static final int CAMERA_REQUEST_TOP = 1888;
    private static final int CAMERA_REQUEST_BOTTOM = 1889;
    private static final int CAMERA_REQUEST_FOOTWEAR = 1890;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout){
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        Calendar myCalendar = Calendar.getInstance();

        private void updateLabel() {

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dateText.setText(sdf.format(myCalendar.getTime()));
        }

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String tag = "SimpleCalendarViewActivity";

        private ImageView calendarToJournalButton;
        private Button selectedDayMonthYearButton;
        private Button currentMonth;
        private ImageView prevMonth;
        private ImageView nextMonth;
        private GridView calendarView;
        private GridCellAdapter adapter;
        private Calendar _calendar;
        private int month, year;
        private final DateFormat dateFormatter = new DateFormat();
        private static final String dateTemplate = "MMMM yyyy";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mContext = getActivity().getApplicationContext();
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                View rootView = inflater.inflate(R.layout.view_calendar, container, false);
                _calendar = Calendar.getInstance(Locale.getDefault());
                month = _calendar.get(Calendar.MONTH) + 1;
                year = _calendar.get(Calendar.YEAR);
                //Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

                prevMonth = (ImageView) rootView.findViewById(R.id.prevMonth);
                prevMonth.setOnClickListener(this);

                currentMonth = (Button) rootView.findViewById(R.id.currentMonth);
                currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

                nextMonth = (ImageView)rootView.findViewById(R.id.nextMonth);
                nextMonth.setOnClickListener(this);

                calendarView = (GridView) rootView.findViewById(R.id.calendar);

                // Initialised
                adapter = new GridCellAdapter(mContext, R.id.calendar_day_gridcell, month, year);
                adapter.notifyDataSetChanged();
                calendarView.setAdapter(adapter);
                return rootView;
            } else {
                View rootView = inflater.inflate(R.layout.add_outfit, container, false);

                imageTop = (ImageView) rootView.findViewById(R.id.imageTop);
                uploadTop = (Button) rootView.findViewById(R.id.uploadPictureTop);

                imageBottom = (ImageView) rootView.findViewById(R.id.imageBottom);
                uploadBottom = (Button) rootView.findViewById(R.id.uploadPictureBottom);

                imageFootwear = (ImageView) rootView.findViewById(R.id.imageFootwear);
                uploadFootwear = (Button) rootView.findViewById(R.id.uploadPictureFootwear);

                dateText = (EditText) rootView.findViewById(R.id.outfit_date);
                mOutfitTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_outfit_type);
                outfitName = (EditText) rootView.findViewById(R.id.outfit_name);
                saveOutfit = (Button) rootView.findViewById(R.id.submit_outfit);

                takePicTop = (Button) rootView.findViewById(R.id.takePictureTop);
                takePicBottom = (Button) rootView.findViewById(R.id.takePictureBottom);
                takePicFootwear = (Button) rootView.findViewById(R.id.takePictureFootwear);

                takePicTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_TOP);
                    }
                });

                takePicBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_BOTTOM);
                    }
                });

                takePicFootwear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_FOOTWEAR);
                    }
                });

                dateText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentDate=Calendar.getInstance();
                        int mYear=mcurrentDate.get(Calendar.YEAR);
                        int mMonth=mcurrentDate.get(Calendar.MONTH);
                        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                myCalendar.set(selectedyear, selectedmonth, selectedday, 0, 0, 0);
                            }
                        },mYear, mMonth, mDay);
                        mDatePicker.setTitle("Select date");
                        mDatePicker.show();
                        updateLabel();
                    }
                });

                uploadTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery("Top");
                    }
                });
                uploadBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery("Bottom");
                    }
                });
                uploadFootwear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery("Footwear");
                    }
                });



                mOutfitTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_outfit_type);

                setupSpinner();

                saveOutfit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(outfitName.getText() == null || outfitName.getText().toString().trim() == "") {
                            Toast.makeText(getActivity(), "Outfit Name is empty", Toast.LENGTH_LONG).show();
                        }
                        else if(dateText.getText() == null || dateText.getText().toString().trim() == "") {
                            Toast.makeText(getActivity(), "Outfit Date is empty", Toast.LENGTH_LONG).show();
                        } else {
                            insertOutfit();
                            insertTopCloth();
                            insertBottomCloth();
                            insertFootwearCloth();
                        }
                    }
                });

                return rootView;
            }
        }

        /**
         * opens the gallery in different conditions, setting up different request codes for different purposes,
         * whether we select an image for top, bottom, or footwear
         **/
        private void openGallery(String type) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            if(type == "Top")
                startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_TOP);
            if(type == "Bottom")
                startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_BOTTOM);
            if(type == "Footwear")
                startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_FOOTWEAR);
        }

        /**
         * sets up the spinner for the dropdown of types for outfit
         * set in the UI
         **/
        private void setupSpinner() {
            // Create adapter for spinner. The list options are from the String array it will use
            // the spinner will use the default layout
            ArrayAdapter outfitTypeSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
                    R.array.array_outfit_type_options, android.R.layout.simple_spinner_item);

            // Specify dropdown layout style - simple list view with 1 item per line
            outfitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            // Apply the adapter to the spinner
            mOutfitTypeSpinner.setAdapter(outfitTypeSpinnerAdapter);

            // Set the integer mSelected to the constant values
            mOutfitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
                    if (!TextUtils.isEmpty(selection)) {
                        if (selection.equals(getString(R.string.outfit_type_casual))) {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_CASUAL;
                        } else if (selection.equals(getString(R.string.outfit_type_trendy))) {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_TRENDY;
                        } else if (selection.equals(getString(R.string.outfit_type_elegant))) {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_ELEGANT;
                        } else if (selection.equals(getString(R.string.outfit_type_sexy))) {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_SEXY;
                        } else if (selection.equals(getString(R.string.outfit_type_exotic))) {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_EXOTIC;
                        } else {
                            mOutfit = ClothesContract.OutfitEntry.TYPE_OTHERS;
                        }
                    }
                }

                // Because AdapterView is an abstract class, onNothingSelected must be defined
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mOutfit = ClothesContract.OutfitEntry.TYPE_CASUAL;
                }
            });
        }

        /**
         * sets up the image, either from gallery or from camera, to the imageview we have defined in our UI
         * based on the request codes that they have returned
         **/
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){
                if (requestCode == PICK_IMAGE_TOP) {
                    Uri selectedImage = data.getData();
                    imageTop.setImageURI(selectedImage);
                }
                if (requestCode == PICK_IMAGE_BOTTOM) {
                    Uri selectedImage = data.getData();
                    imageBottom.setImageURI(selectedImage);
                }
                if (requestCode == PICK_IMAGE_FOOTWEAR) {
                    Uri selectedImage = data.getData();
                    imageFootwear.setImageURI(selectedImage);
                }
                if (requestCode == CAMERA_REQUEST_TOP) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageTop.setImageBitmap(photo);
                }
                if (requestCode == CAMERA_REQUEST_BOTTOM) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageBottom.setImageBitmap(photo);
                }
                if (requestCode == CAMERA_REQUEST_FOOTWEAR) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageFootwear.setImageBitmap(photo);
                }
            }
        }

        public void insertOutfit(){
            String nameString = outfitName.getText().toString().trim();

            ContentValues values = new ContentValues();
            values.put(ClothesContract.OutfitEntry.COLUMN_OUTFIT_NAME, nameString);
            values.put(ClothesContract.OutfitEntry.COLUMN_OUTFIT_TYPE, mOutfit);
            values.put(ClothesContract.OutfitEntry.COLUMN_OUTFIT_DATE, myCalendar.getTimeInMillis() - myCalendar.getTimeInMillis() % 1000);

            newOutfitUri = getActivity().getContentResolver().insert(ClothesContract.OutfitEntry.CONTENT_URI, values);

            if (newOutfitUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(mContext, getString(R.string.insert_outfit_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(mContext, getString(R.string.insert_outfit_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void insertTopCloth(){
            String nameTopString = outfitName.getText().toString().trim() + "Top";

            BitmapDrawable drawable = (BitmapDrawable) imageTop.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            ContentValues values = new ContentValues();
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameTopString);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_TYPE, 0);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

            Uri newUri = getActivity().getContentResolver().insert(ClothesContract.ClothesEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(mContext, getString(R.string.insert_outfit_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(mContext, getString(R.string.insert_outfit_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void insertBottomCloth(){
            String nameBottomString = outfitName.getText().toString().trim() + "Bottom";

            BitmapDrawable drawable = (BitmapDrawable) imageBottom.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            ContentValues values = new ContentValues();
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameBottomString);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_TYPE, 1);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

            Uri newUri = getActivity().getContentResolver().insert(ClothesContract.ClothesEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(mContext, getString(R.string.insert_outfit_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(mContext, getString(R.string.insert_outfit_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void insertFootwearCloth(){
            String nameFootwearString = outfitName.getText().toString().trim() + "Footwear";

            BitmapDrawable drawable = (BitmapDrawable) imageFootwear.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            ContentValues values = new ContentValues();
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameFootwearString);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_TYPE, 2);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
            values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

            Uri newUri = getActivity().getContentResolver().insert(ClothesContract.ClothesEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(mContext, getString(R.string.insert_outfit_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(mContext, getString(R.string.insert_outfit_successful),
                        Toast.LENGTH_SHORT).show();
            }
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
                //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
                setGridCellAdapterToDate(month, year);
            }
            if (v == nextMonth) {
                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                //Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
                setGridCellAdapterToDate(month, year);
            }

        }

        private void setGridCellAdapterToDate(int month, int year) {
            adapter = new GridCellAdapter(mContext, calendar_day_gridcell, month, year);
            _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
            currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
            adapter.notifyDataSetChanged();
            calendarView.setAdapter(adapter);
        }
        @Override
        public void onDestroy() {
            //Log.d(tag, "Destroying View ...");
            super.onDestroy();
        }

        public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
            private static final String tag = "GridCellAdapter";
            private final Context _context;

            private final List<String> list;
            private static final int DAY_OFFSET = 1;
            private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            private final int month, year;
            private int daysInMonth, prevMonthDays;
            private int currentDayOfMonth;
            private int currentWeekDay;
            private Button gridcell;
            private TextView num_events_per_day;
            private final HashMap eventsPerMonthMap;
            private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

            // Days in Current Month
            public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
            {
                super();
                this._context = context;
                this.list = new ArrayList<String>();
                this.month = month;
                this.year = year;

                Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
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
            private String getMonthAsString(int i)
            {
                return months[i];
            }

            private String getWeekDayAsString(int i)
            {
                return weekdays[i];
            }

            private int getNumberOfDaysOfMonth(int i)
            {
                return daysOfMonth[i];
            }

            public String getItem(int position)
            {
                return list.get(position);
            }

            @Override
            public int getCount()
            {
                return list.size();
            }

            /**
             * Prints Month
             *
             * @param mm
             * @param yy
             */
            private void printMonth(int mm, int yy) {
                Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
                // The number of days to leave blank at
                // the start of this month.
                int trailingSpaces = 0;
                int leadSpaces = 0;
                int daysInPrevMonth = 0;
                int prevMonth = 0;
                int prevYear = 0;
                int nextMonth = 0;
                int nextYear = 0;

                int currentMonth = mm - 1;
                String currentMonthName = getMonthAsString(currentMonth);
                daysInMonth = getNumberOfDaysOfMonth(currentMonth);

                Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

                // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
                GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
                Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

                if (currentMonth == 11) {
                    prevMonth = currentMonth - 1;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 0;
                    prevYear = yy;
                    nextYear = yy + 1;
                    Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
                } else if (currentMonth == 0) {
                    prevMonth = 11;
                    prevYear = yy - 1;
                    nextYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 1;
                    Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
                } else {
                    prevMonth = currentMonth - 1;
                    nextMonth = currentMonth + 1;
                    nextYear = yy;
                    prevYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
                }

                // Compute how much to leave before before the first day of the
                // month.
                // getDay() returns 0 for Sunday.
                int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
                trailingSpaces = currentWeekDay;

                Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
                Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
                Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

                if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                    ++daysInMonth;
                }

                // Trailing Month days
                for (int i = 0; i < trailingSpaces; i++) {
                    Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
                    list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
                }

                // Current Month Days
                for (int i = 1; i <= daysInMonth; i++) {
                    Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                    if (i == getCurrentDayOfMonth()) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }

                // Leading Month days
                for (int i = 0; i < list.size() % 7; i++) {
                    Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                    list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
                }
            }

            /**
             * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
             * ALL entries from a SQLite database for that month. Iterate over the
             * List of All entries, and get the dateCreated, which is converted into
             * day.
             *
             * @param year
             * @param month
             * @return
             */
            private HashMap findNumberOfEventsPerMonth(int year, int month) {
                HashMap map = new HashMap<String, Integer>();
                return map;
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                if (row == null) {
                    LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.calendar_item, parent, false);
                }

                // Get a reference to the Day gridcell
                gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
                gridcell.setOnClickListener(this);

                // ACCOUNT FOR SPACING

                Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
                String[] day_color = list.get(position).split("-");
                String theday = day_color[0];
                String themonth = day_color[2];
                String theyear = day_color[3];
                if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                    if (eventsPerMonthMap.containsKey(theday)) {
                        num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                        Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                        num_events_per_day.setText(numEvents.toString());
                    }
                }

                // Set the Day GridCell
                gridcell.setText(theday);
                gridcell.setTag(theday + "-" + themonth + "-" + theyear);
                Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

                if (day_color[1].equals("GREY")) {
                    gridcell.setTextColor(Color.LTGRAY);
                }
                if (day_color[1].equals("WHITE")) {
                    gridcell.setTextColor(Color.WHITE);
                }
                if (day_color[1].equals("BLUE")) {
                    gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
                }
                return row;
            }
            @Override
            public void onClick(View view) {
                String date_month_year = (String) view.getTag();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ViewOutfits.class);
                try {
                    Long millis = dateFormatter.parse(date_month_year).getTime();
                    intent.putExtra("date", millis - millis%1000);
                    startActivity(intent);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            public int getCurrentDayOfMonth()
            {
                return currentDayOfMonth;
            }

            private void setCurrentDayOfMonth(int currentDayOfMonth) {
                this.currentDayOfMonth = currentDayOfMonth;
            }
            public void setCurrentWeekDay(int currentWeekDay) {
                this.currentWeekDay = currentWeekDay;
            }
            public int getCurrentWeekDay()
            {
                return currentWeekDay;
            }
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Calendar";
                case 1:
                    return "Add Outfit";
            }
            return null;
        }
    }
}
