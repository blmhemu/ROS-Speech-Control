package iitk.blmhemu.rosspeechcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    ListView lvSTT;
    FloatingActionButton fabMic;
    TextView tvCurrCmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvSTT = (ListView) findViewById(R.id.lvSTT);
        fabMic = (FloatingActionButton) findViewById(R.id.fabMic);
        tvCurrCmd = (TextView) findViewById(R.id.tvCurrCmd);

        fabMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Command your robot");
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lvSTT.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results));
            String dist = null;
            String dir = null;
            for (String s : results) {
                if (s.contains("stop")) {
                    dir = "Stop";
                    dist = "";
                    break;
                }

                s = s.toLowerCase().trim();
                dist = s.replaceAll("[^0-9]", "").trim();

                if (!dist.isEmpty()) {
                    if (s.contains("left")) {
                        dir = "Left";
                        break;
                    }
                    if (s.contains("right")) {
                        dir = "Right";
                        break;
                    }
                    if (s.contains("front") || s.contains("straight") || s.contains("forward")) {
                        dir = "Front";
                        break;
                    }
                    if (s.contains("back") || s.contains("reverse") || s.contains("backward")) {
                        dir = "Back";
                        break;
                    }
                }
            }

            if (dir != null) {
                String result = dir + " : " + dist;
                tvCurrCmd.setText(result);
            }
        }
    }
}
