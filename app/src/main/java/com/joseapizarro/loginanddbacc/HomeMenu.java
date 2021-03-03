package com.joseapizarro.loginanddbacc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeMenu extends AppCompatActivity {

    private ListView listView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private ArrayList<String> list;
    private ArrayAdapter adapter;

    //Widgets
    private EditText date, detail, name, type, price, qty;
    private Button sendBtn, readBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu2);
        //WIDGET INIT
        listView = findViewById(R.id.listView);
        date = findViewById(R.id.ettDate);
        detail = findViewById(R.id.ettExpDetail);
        name = findViewById(R.id.ettExpName);
        type = findViewById(R.id.ettExpType);
        price = findViewById(R.id.ettPrice);
        qty = findViewById(R.id.ettQty);
        sendBtn = findViewById(R.id.btnSend);
        readBtn2 = findViewById(R.id.btnRead2);
        //ON CLICK
        sendBtn.setOnClickListener(v -> writeIntoFireBase());
        readBtn2.setOnClickListener(v -> readToList());
        //LIST ADAPTER AND ARRAY
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        //FIREBASE UTILS
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
    }

    private void readToList(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("Expenses");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("androidapp", "Content: " + snapshot.getValue().toString());
                    ExpenseDef expense = snapshot.getValue(ExpenseDef.class);
                    list.add(expense.toString());
                    Log.i("androidapp", "Expense Content: " + expense.getExpName());
                    Log.i("androidapp", "Data Key: " + snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("androidapp", "An error has occurred", error.toException());
            }
        });
    }

    //This one is working perfectly fine.
    public void writeIntoFireBase(){
        //Get Data
        String dateTxt = date.getText().toString();
        String detailTxt = detail.getText().toString();
        String nameTxt = name.getText().toString();
        String typeTxt = type.getText().toString();
        String priceTxt = price.getText().toString();
        String qtyTxt = qty.getText().toString();
        //Create Object
        ExpenseDef expense = new ExpenseDef(dateTxt, detailTxt, nameTxt, typeTxt, Float.valueOf(priceTxt), Integer.valueOf(qtyTxt));
        //Get UID (User Identification)
        String userId = mUser.getUid();
        Log.i("androidapp", "User ID: " + userId);
        //Get DataBase Child branch
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        //Generate a new Key
        String key = mDatabase.child("Users").child(userId).child("Expenses").push().getKey();
        //Create a new Map with the Object
        Map<String, Object> postValue = expense.toMap();
        //New HashMap to update the child
        //TODO investigate if we can get this same Hash and use it for modified data or delete date or even read data, who knows :D
        Map<String, Object> childUpdates = new HashMap<>();
        //Yeet the data inside the DB
        childUpdates.put("/Expenses/" + key, postValue);
        //Update the entire DB in case we need to show the new values AKA using a List or RecycleView
        mDatabase.updateChildren(childUpdates);
    }
}