package WordIO;
import java.io.*;

public class WordOperation {

    public String addword(String pathname, String content) {
        synchronized(WordOperation.class){
            content = content.toLowerCase();
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
                    return "No such file AND it was created now !";
                }
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename, true));
                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
                BufferedWriter bw = new BufferedWriter(writer);
                BufferedReader br = new BufferedReader(reader);

                String line = "";
                String duplicate = "";
                String[] addcontent = content.split("( )*:( )*");

                // Check if content is legal
                if (addcontent.length < 2) {
                    return "The content added is illegal !";
                }

                while (line != null) {
                    line = br.readLine();
                    // match "xxx :  yyy"
                    if (line != null) {
                        String[] word_meaning = line.split("( )*:( )*");
                        if (word_meaning.length < 2) {
                            return "Some word-meaning pairs wrong in dic.txt !";
                        }
                        if (word_meaning[0].equals(addcontent[0])){
                            duplicate = word_meaning[0] + " Duplicate";
                            break;
                        }
                    }
                }

                if (duplicate != "") return duplicate;
                else {
                    bw.write(addcontent[0] + " : " + addcontent[1] + '\n');
                    bw.flush();
                    bw.close();
                }

            }
            catch (Exception e) {
                // e.printStackTrace();
                return "IOException occurs";
            }

            return content.split("( )*:( )*")[0] + " Added Successfully !";
        }
    }

    public String getMsg(String pathname, String msg) {
        synchronized(WordOperation.class){
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
                    return "No such file AND it was created now !";
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
                String duplicate = "";

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
