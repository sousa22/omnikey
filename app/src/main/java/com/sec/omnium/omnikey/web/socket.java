package com.sec.omnium.omnikey.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.akts.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;

import static com.sec.omnium.omnikey.UI.Utils.alert;

public class socket {

    public static class Login extends AsyncTask<Void, Void, JSONObject> {

        Context mContext;
        View rootView;

        Socket mSocket;

        String login;
        String pass;

        public Login(Context context, View rootView){
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            EditText loginField = (EditText) rootView.findViewById(R.id.login);
            login = loginField.getText().toString();

            EditText passwordField = (EditText) rootView.findViewById(R.id.pass);
            pass = passwordField.getText().toString();

//            final Intent intent = new Intent(mContext, MenuActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Bundle b = new Bundle();
//
//            b.putString("email", "email");
//            b.putString("name", "name");
//
//            intent.putExtras(b);
//            mContext.startActivity(intent);


            try {
                mSocket = IO.socket("http://192.168.0.11:8080");


                Socket connecting = mSocket.connect();

                // Sending a login
                JSONObject obj = new JSONObject();
                obj.put("login", login);
                obj.put("pass", pass);

                mSocket.emit("login", obj);


                // Receiving a socket disconnect
                mSocket.on(mSocket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        mSocket.off("login");
                        mSocket.off(mSocket.EVENT_DISCONNECT);
                    }

                });

                // Receiving a login response
                mSocket.on("login", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        JSONObject obj = (JSONObject) args[0];
                        Log.d("LOGIN", obj.toString());

                        try {

                            if (obj.getBoolean("successful") == true) {

                                final Intent intent = new Intent(mContext, MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                Bundle b = new Bundle();

                                String email = obj.getJSONObject("user").getJSONObject("local").getString("email").toString();
                                String name = obj.getJSONObject("user").getJSONObject("local").getString("name").toString();

                                b.putString("email", email);
                                b.putString("name", name);

                                intent.putExtras(b);
                                mContext.startActivity(intent);

                            } else {
                                alert(mContext, obj.getString("msg"), "ERROR: " + obj.getInt("error"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

            } catch (URISyntaxException e) {
                alert(mContext, e.toString(), "Socket Error");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

// Funcao abaixo e apenas para teste de criptografia com Socket.IO
// as chaves foram tiradas por seguranca


    public static class SocketTest extends AsyncTask<Void, Void, JSONObject> {

        String publicKeyString = "MIIECzCCAvOgAwIBAgIJAL8DKU2DK7UkMA0GCSqGSIb3DQEBCwUAMIGbMQswCQYDVQQGEwJERjEZ\n" +
                "MBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDERMA8GA1UEBwwIQnJhc2lsaWExFjAUBgNVBAoMDUFr\n" +
                "dW50c3UgR3JvdXAxDTALBgNVBAsMBE9tbmkxEjAQBgNVBAMMCUFLVFMtT01OSTEjMCEGCSqGSIb3\n" +
                "DQEJARYUc291c2FuZHJlaUBnbWFpbC5jb20wHhcNMTUwMjE4MDExNDU3WhcNMTUwMzIwMDExNDU3\n" +
                "WjCBmzELMAkGA1UEBhMCREYxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxETAPBgNVBAcMCEJy\n" +
                "YXNpbGlhMRYwFAYDVQQKDA1Ba3VudHN1IEdyb3VwMQ0wCwYDVQQLDARPbW5pMRIwEAYDVQQDDAlB\n" +
                "S1RTLU9NTkkxIzAhBgkqhkiG9w0BCQEWFHNvdXNhbmRyZWlAZ21haWwuY29tMIIBIjANBgkqhkiG\n" +
                "9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3APhSSQxQsG4UFXUq7/21JLsbsfhlcCbY6sOmoKljds8vC62\n" +
                "OMG1DKNv/rYdr5bWv8juhYsy/t/iBQcJkYR9Xwm7ftlOKdB0q5sf5aieMO5O1h6BDkL3retsDgMi\n" +
                "xy6pWOelzDVs1bhwFfKDzYQJmMgg4muh5duTUv6SSmPDLABHNB4+NxY3dE2z/4YbNDxlF2zWG8BY\n" +
                "iasWQWaU/zzYkRqcNpxKKZq9kIDh4h0SsWPjqva3CaQ8uwjlITBnp5X4xvI4PS/WASNIbdWWTu/7\n" +
                "EbJTECzZL5QBMnwqs4xgt+qEdtLNoH9xnhSZ7wJVxV8KtF2U9Qc0Vms91VwmZKT0vQIDAQABo1Aw\n" +
                "TjAdBgNVHQ4EFgQUzMfcgrZkOdI4x4bce3D75h0mHVIwHwYDVR0jBBgwFoAUzMfcgrZkOdI4x4bc\n" +
                "e3D75h0mHVIwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAfA1L+NMwQZO7hi5/zv4k\n" +
                "jt/Yp9hqa4RHJyV/GOmjg6lan9R7DtCrhu2TWZjttqnIOKNgcEOwywoHFNmPOJsPjM8c32bgeD1Y\n" +
                "SUuJGtdfzfVe4+2INogw6ncXc4VNFqvxRX+PwwrPMMwG9oa+9Yoq3BchYQVbMcbx7uIB5QiWOCOU\n" +
                "fOt7An6Pi/3a7uYgnbx0YCqYWtScm7T6Qe9DvSbJy9MyMmdg7obJMWZAanTIgZHgZ0g6DNetxvEX\n" +
                "ukBNqpvwC7ut2TdUeDElqplUt0lt7rMwenBGAkM5ARu6kl3gsauEbd5ByTsSQgr90h8924LCXsPp\n" +
                "VcC1orzWP/zDZMz8BA==\n";

        String privateKeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDcA+FJJDFCwbhQVdSrv/bUkuxu\n" +
                "x+GVwJtjqw6agqWN2zy8LrY4wbUMo2/+th2vlta/yO6FizL+3+IFBwmRhH1fCbt+2U4p0HSrmx/l\n" +
                "qJ4w7k7WHoEOQvet62wOAyLHLqlY56XMNWzVuHAV8oPNhAmYyCDia6Hl25NS/pJKY8MsAEc0Hj43\n" +
                "Fjd0TbP/hhs0PGUXbNYbwFiJqxZBZpT/PNiRGpw2nEopmr2QgOHiHRKxY+Oq9rcJpDy7COUhMGen\n" +
                "lfjG8jg9L9YBI0ht1ZZO7/sRslMQLNkvlAEyfCqzjGC36oR20s2gf3GeFJnvAlXFXwq0XZT1BzRW\n" +
                "az3VXCZkpPS9AgMBAAECggEBAJBc02PkpZYB+mhsCSGw0crlpNGDwdc7DDq3sNtdQjf0VMO9er9/\n" +
                "CscCLqhY4t2mAb7RnwBtN8bZLDrURUkAQCst/aLb/1ecehFnteRBYZsFoyEH3vJ2qoGp6bq////l\n" +
                "9CRryvRVUdZiMNmVyTn+mzVui3VF3nMuQlrAw0igcR3a0QciUepCxYmoY2cj/5vZVY6NkV2mta8h\n" +
                "68KX0lr0YP2pq+1fi6fWTSmSHB3eot/JiSD5o42fcPuZ3qB4ioywX2GIB7+Ne4q3PhT5fxunkCYk\n" +
                "s1uNrOZ3rcpG5zm1bwAfQzmgtgtL3kJ2jJcgAIJSWHkfln1meU7RehIqBo3/wkECgYEA+YOSzznU\n" +
                "eOSVNaVWe6jCoxWudGuSS7Oyd7vk9ahB5VRa1elWsQWn6M2vouPMo3B2eY854y98OEXW2YRyqhLR\n" +
                "GfHhLfqphzkGr+p38C9sZE1YxpdNz3zGgy/zDLkrTdpSOq+rKQN6Od1kpyS/+5XuAxxU7JY17hNC\n" +
                "i5yeBkRs60kCgYEA4bwApWLizcNrNu2vtUNXRLpkyUda9AYWXjlIriWQcpB4nMQZ4jOwaT8sITEX\n" +
                "MivQm34/LQkiFZvWsqtU3NobRmIN1GLCiStrIz7Yy+tM5Fnai76NqqwkwLvX0k8gDGo7YuIfj33H\n" +
                "SIDH3LSiM3/aN9qRCUJLvV3THpJ+ItciqdUCgYEAqIu2agEYg6l1GU3cg5/WCrAkrkPUCQsT7Umh\n" +
                "Okg0ayN2ULVUeDPn3pzCnfffTjAz951Gby6ubJzQqxKLGlOnqCz5/UMrrUpaOA7cggSm5YJSC6Q9\n" +
                "TYFt30ROnDOhCvAFR9tOdWAB+oCQ808h2GLI6pUC0OYO/AlKlGBxZq5mHPECgYA829f8vuVQ/l+4\n" +
                "hmp53zb3A53Kml9OLRn4u076DIyeJo8uDLzBvJfSOELoi/iqASAT2/yiYrT6yHIg54pQpMj3t9Xs\n" +
                "rdeu7bLL5NTEXaL0abk/Ndex7QlXoQ012TlVv4pVRYumvw1sYbQAr/6PDehCaL3zlhkfa91Qf8Rc\n" +
                "Fk51JQKBgDQT1PiGysUtmbFB13VYhi2dKuoC5l6/whAeAoqHpvdfbyvCYsSLv2zXscnZW88DcbV0\n" +
                "HKaKpELeHEITt8kWwRb/x3z1/nkYnX92u/3JeUWj27Dh8wmRbltvFnb7TJMadZsR6MWQ7EaFG0/7\n" +
                "Fw9bO9Mflfpt9wL9+Qzkr6ynAFIj\n";


        private Context mContext;
        private View rootView;


        Encryption crypt;
        private String key = "";
        private String iv = "";

        private String login;
        private String pass;

        private Socket mSocket;
        private String dat;

        private AlertDialog popup;


        public SocketTest(Context context, View rootView){
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            EditText loginField = (EditText) rootView.findViewById(R.id.login);
            login = loginField.getText().toString();

            EditText passwordField = (EditText) rootView.findViewById(R.id.pass);
            pass = passwordField.getText().toString();

            try {
                mSocket = IO.socket("http://192.168.0.11:8080");


                mSocket.connect();

//                mSocket.emit("init", "message");

                // Sending an object
                JSONObject obj = new JSONObject();
                obj.put("login", login);
                obj.put("pass", pass);


                mSocket.emit("login", obj);

                mSocket.on("fim", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        for(Object a : args){
                            Log.d("TAG", a.toString());
                            dat = a.toString();
                        }
                    }
                });

                // Receiving an object
                mSocket.on("login", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        JSONObject obj = (JSONObject) args[0];
                        Log.d("LOGIN", obj.toString());

                        try {

                            if (obj.getBoolean("successful") == true) {

                                final Intent intent = new Intent(mContext, MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                Bundle b = new Bundle();

                                String email = obj.getJSONObject("user").getJSONObject("local").getString("email").toString();
                                String name = obj.getJSONObject("user").getJSONObject("local").getString("name").toString();

                                b.putString("email", email);
                                b.putString("name", name);

                                intent.putExtras(b);
                                mContext.startActivity(intent);

                            } else {
                                alert(mContext, obj.getString("msg"), "ERROR: " + obj.getInt("error"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });


                mSocket.on("keys", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        for(Object a : args){

                            String data = "teste";

                            try {

                                JSONObject json = new JSONObject(a.toString());

                                key = Encryption.SHA256(privateKeyString, 32);
                                iv = Encryption.SHA256(publicKeyString, 16);

                                crypt = new Encryption();

                                // Step 1:  Alice generates a key pair
                                KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
                                kpg.initialize(256, new SecureRandom());
                                KeyPair kp = kpg.generateKeyPair();

                                // Step 2:  Alice sends the public key and the
                                // 		Diffie-Hellman key parameters to Bob
                                Class dhClass = Class.forName(
                                        "javax.crypto.spec.DHParameterSpec");
                                DHParameterSpec dhSpec = (
                                        (DHPublicKey) kp.getPublic()).getParams();
                                BigInteger aliceG = dhSpec.getG();
                                BigInteger aliceP = dhSpec.getP();
                                int aliceL = dhSpec.getL();
                                byte[] alice = kp.getPublic().getEncoded();

                                // Step 4 part 1:  Alice performs the first phase of the
                                //		protocol with her private key
                                KeyAgreement ka = KeyAgreement.getInstance("DH");
                                ka.init(kp.getPrivate());

                                // Step 4 part 2:  Alice performs the second phase of the
                                //		protocol with Bob's public key
                                String pubkey = json.getString("seed");
                                Log.d("TAG", String.valueOf(kp.getPublic()));
                                Log.d("TAG", String.valueOf(pubkey));

                                KeyFactory kf = KeyFactory.getInstance("DH");
                                X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(pubkey.getBytes());
                                PublicKey pk = kf.generatePublic(x509Spec);
                                ka.doPhase(pk, true);

                                // Step 4 part 3:  Alice can generate the secret key
                                byte secret[] = ka.generateSecret();

                                // Step 6:  Alice generates a DES key
                                SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
                                DESKeySpec desSpec = new DESKeySpec(secret);
                                SecretKey key = skf.generateSecret(desSpec);

                                // Step 7:  Alice encrypts data with the key and sends
                                //		the encrypted data to Bob
                                Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
                                c.init(Cipher.ENCRYPT_MODE, key);
                                byte[] ciphertext = c.doFinal("Stand and unfold yourself".getBytes());


                                Log.d("TAG", String.valueOf(secret));
                                Log.d("TAG0", json.getString("seed"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            Log.d("TAG", key);

                        }
                    }
                });

            } catch (URISyntaxException e) {
                alert(mContext, e.toString(), "Socket Error");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(JSONObject result) {
        }


    }



//      Funcao velha de login por metodo POST, o servidor foi atualizado para Socket.IO
//      Usada como teste de criptografia por AES 256 e chave simentrica
//      enviando o IMEI como texto para geracao das chaves
//
//
//
//    public static class Login extends AsyncTask<Void, Void, String> {
//
//        private Context mContext;
//        private View rootView;
//
//
//        Encryption _crypt;
//        private String imei = "";
//        private String key = "";
//        private String iv = "";
//
//
//        private String login;
//        private String pass;
//
//        HttpResponse response;
//
//        public Login(Context context, View rootView){
//            this.mContext = context;
//            this.rootView = rootView;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            TelephonyManager mngr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//            imei = mngr.getDeviceId();
//
//            try {
//                _crypt = new Encryption();
//                key = Encryption.SHA256("sample", 32); //32 bytes = 256 bit
//                iv = Encryption.generateRandomIV(16); //16 bytes = 128 bit
//
//                key = imei;
//                key += new StringBuilder(imei).reverse().toString();
//
//                iv = imei + 0;
//
//
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            HttpClient httpclient = new DefaultHttpClient();
//
//            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); //Timeout Limit
//            JSONObject json = new JSONObject();
//
//            EditText loginField = (EditText) rootView.findViewById(R.id.login);
//            login = loginField.getText().toString();
//
//            EditText passField = (EditText) rootView.findViewById(R.id.pass);
//            pass = passField.getText().toString();
//
//
////            login = "boludo@tacosmail.jajaja.com";
////            pass = "LaConchaDeTuMadre";
//
//            try {
//
//                // Add your data
//
//                String url = "http://162.243.86.70:8080/post";
////                url = "http://192.168.0.12:8080/post";
//                url = "http://api.akuntsustudios.com.br/post";
//                HttpPost httppost = new HttpPost(url);
//
////                String encoded = _crypt.encrypt(text, key, iv); //encrypt
////                String decoded = _crypt.decrypt(encoded, key,iv); //decrypt
//
//                login = _crypt.encrypt(login, key, iv);
//                pass = _crypt.encrypt(pass, key, iv);
//
////                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//                json.put("login", login);
//                json.put("pass", pass);
//                json.put("imei", imei);
//
//                String data =  _crypt.encrypt(json.toString(), key, iv);
//
//                StringEntity se = new StringEntity(data);
//                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                httppost.setEntity(se);
//
//                response = httpclient.execute(httppost);
//
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (InvalidAlgorithmParameterException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            if(response != null){
//                try {
//
//                    InputStream inputStream = null;
//                    String Fresult = null;
//
//                    inputStream = response.getEntity().getContent();
//                    // json is UTF-8 by default
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//
//                    Fresult = sb.toString();
//
//                    String decoded = _crypt.decrypt(Fresult, key,iv); //decrypt
//
//                    JSONObject jObject = new JSONObject(decoded);
//
//                    final Intent intent = new Intent(mContext, NavigationMain.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    if(jObject.getBoolean("successful")) {
//                        Bundle b = new Bundle();
//                        String email = jObject.getJSONObject("user").getJSONObject("local").getString("email").toString();
//                        String name = jObject.getJSONObject("user").getJSONObject("local").getString("name").toString();
//                        b.putString("email", email);
//                        b.putString("name", name);
//                        intent.putExtras(b);
//                        mContext.startActivity(intent);
////                        Encryption _crypt = new Encryption();
////                        String user = _crypt.decrypt(login, key, iv); //encrypt
////                        alert(mContext, "User: " + jObject.getJSONObject("user").toString(), "POST Response");
//                    } else {
//                        alert(mContext, jObject.getString("msg"), "Error");
//                    }
//
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    alert(mContext, "Error: " + e, "ERROR IOException");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    alert(mContext, "Error: " + e, "ERROR JSONException");
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (InvalidAlgorithmParameterException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                alert(mContext, "Response is null", "POST Error");
//            }
//
//
//        }
//    }
//

}