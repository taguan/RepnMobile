package be.repn.repnmobile;

import android.content.Context;
import com.google.common.base.Optional;

import java.io.*;

public class SecurityHelpers {

    private static final String SECURE_CODE_FILE = "secureCode";
    public static String currentSecureCode;

    public static void writeSecureCode(String secureCode, Context context) throws IOException{
        context.openFileOutput(SECURE_CODE_FILE, Context.MODE_PRIVATE).write(secureCode.getBytes());
    }

    public static Optional<String> getCurrentSecureCode(Context context) throws IOException{
        if(currentSecureCode == null){
            try{
                currentSecureCode = new BufferedReader(new InputStreamReader(context.openFileInput(SECURE_CODE_FILE))).readLine();
            }catch (FileNotFoundException e){
                return Optional.absent();
            }
        }
        return Optional.of(currentSecureCode);

    }

}
