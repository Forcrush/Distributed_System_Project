package WordIO;
import java.io.*;

public class WordOperation {

    public String queryword(String pathname, String word) {
        synchronized(WordOperation.class){
            word = word.toLowerCase();
            String meaning = "";
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
                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
                BufferedReader br = new BufferedReader(reader);
                String line = "";

                if (!word.matches("^([a-zA-Z][\\-]?)*[a-zA-Z]+$")) {
                    return "The word is in illegal form !";
                }

                while (line != null) {
                    line = br.readLine();
                    // match "xxx :  yyy"
                    if (line != null) {
                        String[] word_meaning = line.split("( )*:( )*");
                        if (word_meaning.length < 2) {
                            System.out.println("Some word-meaning pair wrong in dic !");
                        }
                        if (word_meaning[0].equals(word)){
                            meaning = "Meaning of " + word + " : " + word_meaning[1];
                            break;
                        }
                    }
                }
                br.close();

            }
            catch (Exception e) {
                // e.printStackTrace();
                return "IOException occurs";
            }

            if (meaning == "") return word + " NOT FOUND !";
            else return meaning;
        }
    }

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

    public String deleteword(String pathname, String word) {
        synchronized(WordOperation.class){
            word = word.toLowerCase();
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

                // Check if word is legal, can be this form: "well-defined"
                if (!word.matches("^([a-zA-Z][\\-]?)*[a-zA-Z]+$")) {
                    return "The word is in illegal form !";
                }
                int count = 0;

                while (line != null) {
                    line = br.readLine();
                    // match "xxx :  yyy"
                    if (line != null) {
                        String[] word_meaning = line.split("( )*:( )*");
                        if (word_meaning.length < 2) {
                            return "Some word-meaning pairs wrong in dic.txt !";
                        }
                        if (word_meaning[0].equals(word)){
                            duplicate = word + " Duplicate";
                            continue;
                        }
                        else filecontent[count++] = line;
                    }
                }

                if (duplicate == "") return word + " NOT FOUND !";


                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
                BufferedWriter bw = new BufferedWriter(writer);
                for (int i = 0; i < count; i++){
                    bw.write(filecontent[i] + '\n');
                    bw.flush();
                }
                bw.close();

            }
            catch (Exception e) {
                // e.printStackTrace();
                return "IOException occurs";
            }

            return word + " Deleted Successfully !";
        }
    }
}
