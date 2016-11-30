package com.example.javerill.wailpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.ChirpSDKListener;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;

import static com.example.javerill.wailpay.R.layout.item_wallet;

/**
 * Created by javerill on 9/30/2016.
 */

public class WalletContentFragment extends Fragment {
    private static final int RESULT_REQUEST_RECORD_AUDIO = 0;
    public static final String TAG = "WailPay";
    public static final String KEY_SENDER = "SenderID";
    public static final String KEY_RECEIVER = "ReceiverID";
    //private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
    private int soundSuccess;
    private String TransID;
    private static final String CREATE_TRANS = "http://wailpay.hol.es/createTrans.php";
    private static final String GET_TRANS = "http://wailpay.hol.es/getTrans.php";
    private static final String CONFIRM_TRANS = "http://wailpay.hol.es/confirmTrans.php";
    public ChirpSDK chirpSDK;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> productsList;
    LayoutInflater inflater;

    private ChirpSDKListener chirpSDKListener = new ChirpSDKListener() {
        /*------------------------------------------------------------------------------
         * onChirpHeard is triggered when a Chirp tone is received.
         * Obtain the chirp's 10-character identifier with `getIdentifier`.
         *----------------------------------------------------------------------------*/
        @Override
        public void onChirpHeard(Chirp chirp) {
            /*------------------------------------------------------------------------------
             * We're encoding the properties of each gem within the identifier:
             * Position, orientation, and colour.
             *
             * Create and display a new gem with these properties.
             *----------------------------------------------------------------------------*/
            final String TransID = chirp.getIdentifier();

            new ReceiveTrans().execute();
        }

        /*------------------------------------------------------------------------------
         * onChirpError is triggered when an error occurs -- for example,
         * authentication failure or muted device.
         *
         * See the documentation on ChirpError for possible error codes.
         *----------------------------------------------------------------------------*/
        @Override
        public void onChirpError(ChirpError chirpError) {
            Log.d(TAG, "Identifier received error: " + chirpError.getMessage());
        }
    };

    class ReceiveTrans extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating Transaction, Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_TRANS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            final MainActivity b = (MainActivity) getContext();
                            b.layoutBG.getForeground().setAlpha(220);

                            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View popupR = layoutInflater.inflate(R.layout.popup_confirm, null);
                            final PopupWindow popupR2 = new PopupWindow(popupR, 600,500, true);
                            popupR2.setFocusable(true);
                            popupR2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    b.layoutBG.getForeground().setAlpha(0);
                                }
                            });
                            popupR2.setBackgroundDrawable(new ColorDrawable());
                            popupR2.dismiss();
                            popupR2.showAtLocation(popupR, Gravity.CENTER, 0, 0);

                            String[] separated = response.split(";");
                            ((TextView)popupR.findViewById(R.id.tvReceivedAmount)).setText(separated[1]);
                            ((TextView)popupR.findViewById(R.id.txtSender)).setText(separated[0]);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Volley error: " + error.getMessage());
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("TransID", TransID);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
            return null;
        }

        protected void onPostExecute(String result) {

        }
    }

    private boolean doWeHaveRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, RESULT_REQUEST_RECORD_AUDIO);
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chirpSDK = new ChirpSDK(getContext(), "tRO8YP7HsZeKVmlimYnXWKfOl", "TpQ56eYPa4qiClWMoJ3f3lAIKJiWW3bNHiryz0Dsf69Ttt6wrq");
        chirpSDK.setListener(chirpSDKListener);
        if (doWeHaveRecordAudioPermission()) {
            chirpSDK.start();
        }

        final View view = inflater.inflate(R.layout.item_wallet, container, false);
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_send, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 600,420, true);

        Button btnSend = (Button) view.findViewById(R.id.btnSend);
        Button btnReceive = (Button) view.findViewById(R.id.btnReceive);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final MainActivity mainActivitySend = (MainActivity) getContext();
                mainActivitySend.layoutBG.getForeground().setAlpha(220);

                popupWindow.setFocusable(true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mainActivitySend.layoutBG.getForeground().setAlpha(0);
                    }
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.dismiss();
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                final TextView linkSend = (TextView)popupView.findViewById(R.id.linkSend);
                final TextView linkCancel = (TextView)popupView.findViewById(R.id.linkCancel);

                linkCancel.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                linkSend.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        //SendGetStartedNotification(ParseUser user);
                        new CreateTrans().execute();
                    }
                });
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final MainActivity mainActivityReceive = (MainActivity) getContext();
                mainActivityReceive.layoutBG.getForeground().setAlpha(220);

                popupWindow.setFocusable(true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mainActivityReceive.layoutBG.getForeground().setAlpha(0);
                    }
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.dismiss();
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                final TextView linkSend = (TextView)popupView.findViewById(R.id.linkSend);
                final TextView linkCancel = (TextView)popupView.findViewById(R.id.linkCancel);

                linkCancel.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                linkSend.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        //SendGetStartedNotification(ParseUser user);
                        new CreateTrans().execute();
                    }
                });

            }
        });

        return view;
    }

    class CreateTrans extends AsyncTask<String, String, String> {
        String identifier = "0000000123";
        //EditText etAmount = (EditText)popupView.findViewById(R.id.etAmount);
        //EditText etPassKey = (EditText)popupView.findViewById(R.id.etPasskey);
        //final String amount = etAmount.getText().toString();
        //final String passKey = etPassKey.getText().toString();

        final String amount = "50000";
        final String passKey = "1234";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating Transaction, Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(final String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_TRANS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();

                            int transID = Integer.parseInt(response);
                            String resp = response.toString().trim();
                            Firebase ref = new Firebase("https://wailpay-b8502.firebaseio.com/transaction/"+resp);

                            String identifier = String.format("%010d", transID);
                            Log.d("test", identifier);
                            //((TextView)getView().findViewById(R.id.txtReceived)).setText(identifier);

//                            ref.addValueEventListener(new ValueEventListener() {
//                                public void onDataChange(DataSnapshot snapshot) {
//                                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                                        Transact post = postSnapshot.getValue(Transact.class);
//                                        ((TextView) findViewById(R.id.txtStatus)).setText(post.getStatus());
//                                    }
//                                }
//
//                                public void onCancelled(FirebaseError firebaseError) {
//                                    System.out.println("The read failed: " + firebaseError.getMessage());
//                                }
//                            });
                            chirpSDK.chirp(new Chirp(identifier));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    Log.d("test1", amount);
                    Log.d("test2", passKey);
                    params.put(KEY_SENDER,"12");
                    params.put("Amount",amount);
                    params.put("PassKey",passKey);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
            return null;
        }

        protected void onPostExecute(String file_url) {

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(item_wallet, parent, false));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 18;

        public ContentAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
