package testing.example.com.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView mConditionTextView;
    Button mButtonSunny;
    Button mButtonFoggy;
    ListView mListView;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference mRootRef;
    DatabaseReference mConditionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConditionTextView = (TextView) findViewById(R.id.textViewCondition);
        mButtonSunny = (Button) findViewById(R.id.buttonSunny);
        mButtonFoggy = (Button) findViewById(R.id.buttonFoggy);
        mListView = (ListView) findViewById(R.id.listView);

        if (mRootRef == null) {
            db.setPersistenceEnabled(true);
            mRootRef = db.getReference();
            mConditionRef = mRootRef.child("messages"); //This check for a child named condition below the root of the database
        }
        mButtonSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("Sunny!");
            }
        });

        mButtonFoggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("Foggy!");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Don't use addChildEventListener in case you want to display in a list
/*
        mConditionRef.addChildEventListener(new ChildEventListener() {           //This Listener is called for each child
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {     //This will be called for initial child already added + all new child created

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;   */


        /*Use this in case Using List otherwise use addValue or addChildValueListeners
          This is part of FirebaseUI
         */
        final FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, mConditionRef) {
            @Override
            protected void populateView(View v, String model, int position) {        //String model is basically === dataSnapshot.getValue(String.class)
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        mListView.setAdapter(adapter);


        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //Will be called when "condition" value updates in the database
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                mConditionTextView.setText("" + map.get("hello") + " " + map.get("world"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  //In case any error occurs

            }
        });

    }

}
