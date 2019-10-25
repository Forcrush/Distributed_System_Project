package message;
import java.io.*;

public class MsgOperation {

    public String getMsg(String pathname, String msg) {
        synchronized(MsgOperation.class){
            String result = "";
            try {
            /* // This can be used to test concurrency
            try {
                Thread.currentThread().sleep(4000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
                File filename = new File(pathname);
                if (!filename.exists()) {
                    filename.createNewFile();
                    System.out.println("No such file AND it was created now !");
                    // return "No such file AND it was created now !";
                }
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename, true));
                BufferedWriter bw = new BufferedWriter(writer);

                String[] content = msg.split("( )*:( )*");
                int clientPos = Integer.parseInt(content[0]);
                if (content.length > 2 && content[2].length() != 0){
                    bw.write(content[1] + " : " + content[2] + '\n');
                    bw.flush();
                    bw.close();
                }

                // get lines of the file
                FileReader freader = new FileReader(filename);
                LineNumberReader lnreader = new LineNumberReader(freader);
                lnreader.skip(Long.MAX_VALUE);
                int lines = lnreader.getLineNumber();
                lnreader.close();

                String[] filecontent = new String[lines];
                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
                BufferedReader br = new BufferedReader(reader);

                String line = "";

                int count = 0;

                while (line != null) {
                    line = br.readLine();
                    if (line != null) {
                        filecontent[count++] = line;
                    }
                }
                if (clientPos < count) {
                    for (int i = clientPos; i < count; i++) {
                        result += filecontent[i] + "\n";
                    }
                    result = String.valueOf(count) + " :: " + result;
                }
                else {
                    result = String.valueOf(count) + " :: " + "";
                }

            }
            catch (Exception e) {
                // e.printStackTrace();
                return "IOException occurs";
            }

            return result;
        }
    }
}
