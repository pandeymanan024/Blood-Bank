package com.example.blood_bank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.blood_bank.R;
import com.example.blood_bank.Utils.Endpoints;
import com.example.blood_bank.Utils.VolleySingleton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt,cityEt,bloodGroupEt,passwordEt,mobileEt;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt =findViewById(R.id.name);
        cityEt =findViewById(R.id.city);
        bloodGroupEt =findViewById(R.id.blood_group);
        passwordEt =findViewById(R.id.password);
        mobileEt =findViewById(R.id.number);
        submitButton =findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name , city , blood_group , password , mobile;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                blood_group = bloodGroupEt.getText().toString();
                password = passwordEt.getText().toString();
                mobile = mobileEt.getText().toString();
                //showMessage(name+"\n"+city+"\n"+blood_group+"\n"+password+"\n"+mobile);
                if(isValid(name,city,blood_group,password,mobile)){
                    register(name,city,blood_group,password,mobile);
                }
            }
        });
    }

    private void register (final String name , final String city , final String blood_group , final String password , final String mobile){
        StringRequest stringRequest = new StringRequest(POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    Toast.makeText(RegisterActivity.this,"222",Toast.LENGTH_SHORT).show();
                    GLSurfaceView glView = new GLSurfaceView(getApplicationContext());
                    glView.setEGLContextClientVersion(2);
                    glView.setPreserveEGLContextOnPause(true);
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    RegisterActivity.this.finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"1111",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("Volley",error.getMessage());
            }
        }){
            @Override
            protected Object clone() throws CloneNotSupportedException {
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("city",city);
                params.put("blood_group",blood_group);
                params.put("password",password);
                params.put("mobile",mobile);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name ,String city , String blood_group, String password , String mobile )
    {
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");
        if(name.isEmpty()){
            showMessage("Name is empty");
            return false;
        }
        else if(city.isEmpty()){
            showMessage("city name is empty");
            return false;
        }
        else if(!valid_blood_groups.contains(blood_group)){
            showMessage("Not a valid Blood group choose form " + valid_blood_groups);
            return false;
        }
        else if(mobile.length() != 10){
            showMessage("Invalid mobile number");
            return false;
        }
        return true;
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
