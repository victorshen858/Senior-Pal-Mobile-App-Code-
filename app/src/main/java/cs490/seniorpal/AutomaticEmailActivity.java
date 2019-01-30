package cs490.seniorpal;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AutomaticEmailActivity extends AppCompatActivity {
    Mail m;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    static ArrayList<Person> emergencyContactsArray = new ArrayList<Person>();
    static ArrayList<String> smsTextNumbersArray = new ArrayList<String>();
    Boolean smsTextSent = false;
    EditText FirstName, LastName, Relation, Phone, UserEmail, UserPassword, UserFirstName, UserLastName;
    Spinner spinnerCarrier;
    ArrayAdapter<String> myAdapter;
    String spinnerTopic="";
    Button btnAddPerson, btnSaveEmailInfo,btnClearContacts,btnShowContacts;
    Boolean isEmergencyHappening = false;
    Toast toast;
    LinearLayout toastLayout;
    TextView toastTV;
    static String usrEmail="",usrPassword="",usrFirstName="",usrLastName="";
    String fn="",ln="",rel="",phone="";
    StringBuilder saveListStr;
    String strList = "";
    String userIdentity="";
    String gpsLocation="";
    String isEmergencyHappeningString="";
    TextView numContacts;
    Person newPerson;
    String userEmailSavedString="", userPasswordSavedString="",userFNSavedString="", userLNSavedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_email);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null) { ab.setDisplayHomeAsUpEnabled(true); }


        //TEXTVIEW OF NUMBER OF CONTACTS
        numContacts = (TextView) findViewById(R.id.numContacts);
        //EDIT TEXT SETUP
        FirstName = (EditText) findViewById(R.id.etFirstName);
        LastName = (EditText) findViewById(R.id.etLastName);
        Relation = (EditText) findViewById(R.id.etRelation);
        Phone = (EditText) findViewById(R.id.etPhone);
        UserEmail = (EditText) findViewById(R.id.etEmail);
        UserPassword = (EditText) findViewById(R.id.etPassword);
        UserFirstName = (EditText) findViewById(R.id.userFirstName);
        UserLastName = (EditText) findViewById(R.id.userLastName);
        //SPINNER OF CARRIERS SETUP
        spinnerCarrier = (Spinner) findViewById(R.id.spinnerOfCarriers);
        spinnerTopic = spinnerCarrier.getSelectedItem().toString().toUpperCase();
        myAdapter = new ArrayAdapter(this, R.layout.spinner_layout, new ArrayList<String>());
        myAdapter.add("AT&T");
        myAdapter.add("T-Mobile");
        myAdapter.add("Verizon");
        myAdapter.add("Sprint");
        myAdapter.add("Virgin Mobile");
        myAdapter.add("Tracfone");
        myAdapter.add("Boost Mobile");
        myAdapter.add("Cricket");
        myAdapter.add("Nextel");
        myAdapter.add("Alltel");
        myAdapter.add("Ptel");
        myAdapter.add("Suncom");
        myAdapter.add("Qwest");
        myAdapter.add("U.S. Cellular");
        spinnerCarrier.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        //Get Saved Contact List of Emergency Contacts(if there are any)
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sList = sharedPref.getString("textList", "");
        String[] items = sList.split(",");
        smsTextNumbersArray = new ArrayList<String>();
        if(items.length>0) {
            for (int i = 0; i < items.length; i++) {
                if (!(items[i].equals(""))) {
                    smsTextNumbersArray.add(items[i]);
                }
            }
        }
        userEmailSavedString = sharedPref.getString("userEmail", "seniorpalapp@gmail.com");
        userPasswordSavedString = sharedPref.getString("userPassword", "Diannaoseniorpal");
        userIdentity = ""+sharedPref.getString("userFirstName", "No User First Name Saved")+" "+sharedPref.getString("userLastName", "No User Last Name Saved");
        userFNSavedString=sharedPref.getString("userFirstName", "");
        userLNSavedString=sharedPref.getString("userLastName", "");
        if((userEmailSavedString.equals(""))||(userPasswordSavedString.equals(""))||(userFNSavedString.equals(""))||(userLNSavedString.equals(""))) {
            //do nothing
        }
        else{ //IF USER ALREADY FILLED THIS THEN IT SHOWS
            UserEmail.setText("" + userEmailSavedString);
            UserPassword.setText("" + userPasswordSavedString);
            UserFirstName.setText("" + userFNSavedString);
            UserLastName.setText("" + userLNSavedString);
        }

        //USER IN LIFE THREATENING SITUATION
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            isEmergencyHappeningString = bundle.getString("emergencyFlag");
            gpsLocation = bundle.getString("gpsLocation");
            //EMERGENCY IS INDEED HAPPENING EITHER FROM EmergencyButton.java or BathSaunaWalk.java
            if(isEmergencyHappeningString.equals("true")){
                isEmergencyHappening=true;
                setUpEmail(); //puts data in email to send asynchronously
            }
            else{
                isEmergencyHappening=false;
            }


        }
        //user didn't type in any contacts
        else{
            if(emergencyContactsArray.size()<=0){
                toast = Toast.makeText(AutomaticEmailActivity.this, "You need to add contacts before this feature will work properly.", Toast.LENGTH_SHORT);
                toastLayout = (LinearLayout) toast.getView();
                toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(800);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
        //update contact list count at start of app
        numContacts.setText("You currently have " +emergencyContactsArray.size()+ " contacts stored in your Emergency Contact List.");


        //BUTTON THAT SHOWS ALL THE CONTACTS
        btnShowContacts = (Button) findViewById(R.id.button_show_contacts);
        btnShowContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DISPLAY TOAST
                if((emergencyContactsArray.size()>0)) {
                    toast = Toast.makeText(AutomaticEmailActivity.this, "" + emergencyContactsArray.toString(), Toast.LENGTH_SHORT);
                }
                else{
                    toast = Toast.makeText(AutomaticEmailActivity.this, "Contact List is Empty.", Toast.LENGTH_SHORT);
                }
                toastLayout = (LinearLayout) toast.getView();
                toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(800);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();
        }
        });


        //BUTTON THAT CLEARS ALL CONTACTS
        btnClearContacts = (Button) findViewById(R.id.btn_clear_contacts);
        btnClearContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((emergencyContactsArray!=null)&&(smsTextNumbersArray!=null)){
                    if((emergencyContactsArray.size()>0)){
                        emergencyContactsArray.clear();
                        smsTextNumbersArray.clear();
                        toast = Toast.makeText(AutomaticEmailActivity.this, "All Emergency Contacts Have Been Cleared.", Toast.LENGTH_SHORT);
                        toastLayout = (LinearLayout) toast.getView();
                        toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(58);
                        toastTV.setWidth(20500);
                        toastTV.setHeight(500);
                        toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                        toast.show();
                        numContacts.setText("You currently have " +emergencyContactsArray.size()+ " contacts stored in your Emergency Contact List.");
                    }
                }
                else{
                    numContacts.setText("You have no contacts to clear.");

                }
            }
        });

        //BUTTON THAT ADDS A PERSON TO THE CONTACT LIST
        btnAddPerson = (Button) findViewById(R.id.btn_add_person);
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn = FirstName.getText().toString();
                ln = LastName.getText().toString();
                rel = Relation.getText().toString();
                phone = Phone.getText().toString();
                spinnerTopic = spinnerCarrier.getSelectedItem().toString().toUpperCase();
                if((fn.equals("")||(ln.equals(""))||(rel.equals(""))||(phone.equals(""))||(spinnerTopic.equals("")))){
                    toast = Toast.makeText(AutomaticEmailActivity.this, "Please type in non-blank fields",Toast.LENGTH_LONG);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(550);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else if((fn==null)||(ln==null)||(rel==null)||(phone==null)||(spinnerTopic==null)){
                    toast = Toast.makeText(AutomaticEmailActivity.this, "Please type in non-blank fields",Toast.LENGTH_LONG);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(550);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else{
                    try {
                        System.out.println("fn = "+fn);
                        System.out.println("ln = "+ln);
                        System.out.println("rel = "+rel);
                        System.out.println("phone = " + phone);
                        System.out.println("spinnerTopic = " + spinnerTopic);
                        newPerson = new Person(fn,ln,rel,phone,spinnerTopic);
                        System.out.println("Tried to add person-> " + newPerson.toString());
                        //ADD Person and SMS Text String to arrays
                        emergencyContactsArray.add(newPerson);
                        smsTextNumbersArray.add(newPerson.getSendTextString());
                        numContacts.setText("You currently have " + emergencyContactsArray.size() + " contacts stored in your Emergency Contact List.");
                        //DISPLAY TOAST
                        toast = Toast.makeText(AutomaticEmailActivity.this, ""+FirstName.getText().toString()+" "+LastName.getText().toString()+" was successfully added to the Emergency Contact List.", Toast.LENGTH_SHORT);
                        toastLayout = (LinearLayout) toast.getView();
                        toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(58);
                        toastTV.setWidth(20500);
                        toastTV.setHeight(550);
                        toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                        toast.show();

                        //Build String of SMS Text Numbers and make a string separating each contact by a ',' deliminator
                        saveListStr = new StringBuilder();
                        for (String s : smsTextNumbersArray) {
                            saveListStr.append(s);
                            saveListStr.append(",");
                        }
                        strList = saveListStr.toString();
                        sharedPref = getPreferences(Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("textList", strList);
                        editor.commit();
                        //after saving data we must clear the form for a new entry
                        FirstName.setText("");
                        LastName.setText("");
                        Relation.setText("");
                        Phone.setText("");
                    } catch (Exception e) {
                        Toast.makeText(AutomaticEmailActivity.this,"An error occurred while trying to save the following person: "+newPerson.toString(),Toast.LENGTH_SHORT).show();
                        System.out.println("" + e);
                        numContacts.setText("Unable to add contact to Emergency Contact List.");
                    }
                }
                numContacts.setText("You currently have " +emergencyContactsArray.size()+ " contacts stored in your Emergency Contact List.");

            }
        });

        //SAVE USER EMAIL AND PASSWORD BUTTON
        btnSaveEmailInfo = (Button) findViewById(R.id.btn_save_email_info);
        btnSaveEmailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((UserEmail.getText().toString().equals(""))||(UserPassword.getText().toString().equals(""))||(UserFirstName.getText().toString().equals(""))||(UserLastName.getText().toString().equals(""))){
                    Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email and password",Toast.LENGTH_SHORT).show();
                }
                else if(UserEmail.getText().toString().equals("")){
                    Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email address",Toast.LENGTH_SHORT).show();
                }
                else if(UserPassword.getText().toString().equals("")){
                    Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email password",Toast.LENGTH_SHORT).show();
                }
                else if(UserFirstName.getText().toString().equals("")){
                    Toast.makeText(AutomaticEmailActivity.this, "Please type in your first name",Toast.LENGTH_SHORT).show();
                }
                else if(UserLastName.getText().toString().equals("")){
                    Toast.makeText(AutomaticEmailActivity.this, "Please type in your last name",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        usrEmail = UserEmail.getText().toString();
                        usrPassword = UserPassword.getText().toString();
                        usrFirstName = UserFirstName.getText().toString();
                        usrLastName = UserLastName.getText().toString();
                        //Build String of SMS Text Numbers and make a string separating each contact by ',' deliminator
                        saveListStr = new StringBuilder();
                        for (String s : smsTextNumbersArray) {
                            saveListStr.append(s);
                            saveListStr.append(",");
                        }
                        strList = saveListStr.toString();
                        toast = Toast.makeText(AutomaticEmailActivity.this, "Email and User Information Successfully Saved",Toast.LENGTH_SHORT);
                        toastLayout = (LinearLayout) toast.getView();
                        toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(58);
                        toastTV.setWidth(20500);
                        toastTV.setHeight(550);
                        toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                        toast.show();
                        //ACTUAL SAVING USING SHARED PREFERENCES
                        sharedPref = getPreferences(Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("userEmail", usrEmail);
                        editor.putString("userPassword", usrPassword);
                        editor.putString("userFirstName", usrFirstName);
                        editor.putString("userLastName", usrLastName);
                        editor.commit();
                    } catch (Exception e) {
                        System.out.println("Error getting user email and password info");
                    }
                }
            }
        });

    }//end of onCreate() method

    //Setup Email with list of emergency contacts
    public void setUpEmail(){
        m = new Mail(userEmailSavedString, userPasswordSavedString);
        new SendMailTask().execute(m); //sends not on main thread to avoid exception
    }
    //AsynchTask to send Email automatically without user interventation, avoids os error by manipulating main thread
    private class SendMailTask extends AsyncTask<Mail, Void, Void> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AutomaticEmailActivity.this, "Please wait", "Sending mail", true, false);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
        @Override
        protected Void doInBackground(Mail... messages) {
            try {
                String[] toArr = new String[smsTextNumbersArray.size()];
                toArr = smsTextNumbersArray.toArray(toArr);// This is an array, you can add more emails, just separate them with a coma
                m.setTo(toArr); // load array to setTo function
                m.setFrom(userEmailSavedString); // who is sending the email
                m.setSubject("EMERGENCY!");
                m.setBody(" " + userIdentity + " has listed you as an emergency contact and is now in a life-threatening situation that requires immediate medical attention! "
                        + userIdentity + "'s " + gpsLocation + ". Please contact 911 and other related parties and pass on these GPS coordinates to help rescue "+userIdentity+ "!!! Thank you for your assistance.");
                if(m.send()) {
                    // success
                    smsTextSent = true;
                    System.out.println("SMS Text sent successfully to the following recipients:\n"+m.getRecipient());
                    Intent helpScreenIntent = new Intent(AutomaticEmailActivity.this,HelpOnTheWayScreen.class);
                    startActivity(helpScreenIntent);
                }
                else{// failure
                    smsTextSent = false;
                    System.out.println("SMS text failed to send due to permission error with mail server.");
                    m.returnError();
                    String url = "https://www.google.com/settings/security/lesssecureapps";
                    Intent secureSetup = new Intent(Intent.ACTION_VIEW);
                    secureSetup.setData(Uri.parse(url));
                    startActivity(secureSetup);
                    Toast.makeText(AutomaticEmailActivity.this,"Error: Could not send text message to listed contacts due to email security settings, please login and 'turn on' access for third party apps.", Toast.LENGTH_LONG).show();

                }
            }
            catch (Exception e) {
                smsTextSent = false;
                System.out.println("Exception occurred while trying to send SMS text: " + e);
            }
            return null;
        }
    }

    //Saves Data When User Exits Automatically
    @Override
    protected void onPause() {
        super.onPause();
        //Build String of SMS Text Numbers and make a string separating each contact by ',' deliminator
        saveListStr = new StringBuilder();
        for (String s : smsTextNumbersArray) {
            saveListStr.append(s);
            saveListStr.append(",");
        }
        strList = saveListStr.toString();

        if((UserEmail.getText().toString().equals(""))||(UserPassword.getText().toString().equals(""))||(UserFirstName.getText().toString().equals(""))||(UserLastName.getText().toString().equals(""))){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email and password",Toast.LENGTH_SHORT).show();
        }
        else if(UserEmail.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email address",Toast.LENGTH_SHORT).show();
        }
        else if(UserPassword.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email password",Toast.LENGTH_SHORT).show();
        }
        else if(UserFirstName.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in your first name",Toast.LENGTH_SHORT).show();
        }
        else if(UserLastName.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in your last name",Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                usrEmail = UserEmail.getText().toString();
                usrPassword = UserPassword.getText().toString();
                usrFirstName = UserFirstName.getText().toString();
                usrLastName = UserLastName.getText().toString();

                //ACTUAL SAVING USING SHARED PREFERENCES
                sharedPref = getPreferences(Context.MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("textList", strList);
                editor.putString("userEmail", usrEmail);
                editor.putString("userPassword", usrPassword);
                editor.putString("userFirstName", usrFirstName);
                editor.putString("userLastName", usrLastName);
                editor.commit();
            } catch (Exception e) {
                System.out.println("Error getting user email and password info");
            }
        }

    }


    public static String getUserFirstName(){
        return usrFirstName;
    }

    public static String getUserLastName(){
        return usrLastName;
    }

    public static int getNumberOfEmergencyContacts(){
        return emergencyContactsArray.size();
    }

    public void onBackPressed() {
        //Build String of SMS Text Numbers and make a string seperating each contact by ',' deliminator
        saveListStr = new StringBuilder();
        for (String s : smsTextNumbersArray) {
            saveListStr.append(s);
            saveListStr.append(",");
        }
        strList = saveListStr.toString();

        if((UserEmail.getText().toString().equals(""))||(UserPassword.getText().toString().equals(""))||(UserFirstName.getText().toString().equals(""))||(UserLastName.getText().toString().equals(""))){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email and password",Toast.LENGTH_SHORT).show();
        }
        else if(UserEmail.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email address",Toast.LENGTH_SHORT).show();
        }
        else if(UserPassword.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in a valid email password",Toast.LENGTH_SHORT).show();
        }
        else if(UserFirstName.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in your first name",Toast.LENGTH_SHORT).show();
        }
        else if(UserLastName.getText().toString().equals("")){
            Toast.makeText(AutomaticEmailActivity.this, "Please type in your last name",Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                usrEmail = UserEmail.getText().toString();
                usrPassword = UserPassword.getText().toString();
                usrFirstName = UserFirstName.getText().toString();
                usrLastName = UserLastName.getText().toString();
                //ACTUAL SAVING USING SHARED PREFERENCES
                sharedPref = getPreferences(Context.MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("textList", strList);
                editor.putString("userEmail", usrEmail);
                editor.putString("userPassword", usrPassword);
                editor.putString("userFirstName", usrFirstName);
                editor.putString("userLastName", usrLastName);
                editor.commit();
            } catch (Exception e) {
                System.out.println("Error getting user email and password info");
            }
        }
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
