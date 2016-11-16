package com.johnfe.martesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class BaseDatos extends AppCompatActivity {

    //private FirebaseAuth mAuth;
    Intent intent;
    EditText mensaje;
    EditText tema;
    TextView databaseMessage;
    Button btnEnviar;
    Button btnTema;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    @SuppressWarnings("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_datos);
        intent= getIntent();

        mensaje= (EditText) findViewById(R.id.editText);
        tema= (EditText) findViewById(R.id.etTema);
        databaseMessage= (TextView) findViewById(R.id.txtMensaje);
        btnEnviar= (Button) findViewById(R.id.button2);
        btnTema= (Button) findViewById(R.id.btnTema);

        myRef = database.getReference("mensaje");
        myRef.setValue("Hola mundo!");
        MyFirebaseInstanceIDService instanceIDService = new MyFirebaseInstanceIDService();
        instanceIDService.onTokenRefresh();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valor = dataSnapshot.getValue(String.class);
                Log.d("BaseDatos", "Value is: " + valor);
                databaseMessage.setText( "El mensaje es: " + valor);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("BaseDatos", "Failed to read value.", error.toException());
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(mensaje.getText().toString().trim());
            }
        });

        btnTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic(tema.getText().toString().trim());
            }
        });




    }
}
