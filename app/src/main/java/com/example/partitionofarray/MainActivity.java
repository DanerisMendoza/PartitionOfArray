package com.example.partitionofarray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private int currentDialogIndex = 0;

    // Keep track of the dialogs
    private ArrayList<Dialog> dialogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display();
    }

    public void display() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("President");
        arrayList.add("President");
        arrayList.add("President");
        arrayList.add("Vice");
        arrayList.add("Vice");
        arrayList.add("Secretary");

        // Create a HashMap to store the separated lists
        HashMap<String, ArrayList<String>> separatedLists = new HashMap<>();

        // Iterate over the ArrayList
        for (String value : arrayList) {
            if (separatedLists.containsKey(value)) {
                // If the value already exists as a key in the HashMap,
                // add the element to the existing ArrayList
                separatedLists.get(value).add(value);
            } else {
                // If the value is encountered for the first time,
                // create a new ArrayList and add the element to it
                ArrayList<String> newList = new ArrayList<>();
                newList.add(value);
                separatedLists.put(value, newList);
            }
        }

        // Calculate the desired height based on the number of items in the list
        int desiredHeight = 500;
        int itemHeight = 500;
        int totalItems = arrayList.size();
        int calculatedHeight = Math.min(desiredHeight, itemHeight * totalItems);

        // Iterate over the separated lists
        for (String key : separatedLists.keySet()) {
            ArrayList<String> list = separatedLists.get(key);

            // Create a new dialog for each separated list
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);

            // Get the GridView from the dialog's layout
            GridView gridView = dialog.findViewById(R.id.gridView);

            // Create an ArrayList to hold the combined separated list
            ArrayList<String> combinedList = new ArrayList<>();

            // Add the key as a separator in the combined list
            combinedList.add(key);

            // Add the items from the current list to the combined list
            combinedList.addAll(list);

            // Create an ArrayAdapter to display the list items
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combinedList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView textView;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        textView = convertView.findViewById(android.R.id.text1);
                        convertView.setTag(textView);
                    } else {
                        textView = (TextView) convertView.getTag();
                    }

                    // Determine if the current item is a separator or an item
                    String item = getItem(position);

                    textView.setText(item);

                    return convertView;
                }
            };

            // Set the adapter to the GridView
            gridView.setAdapter(adapter);

            // Set the item click listener inside the dialog's show listener
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Set the fixed size for the dialog's window
            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialogWindow.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = calculatedHeight;
                dialogWindow.setAttributes(layoutParams);
            }

            dialogs.add(dialog); // Add the dialog to the list

            // Show only the first dialog, hide the rest
            if (currentDialogIndex == 0) {
                dialog.show();
            } else {
                dialog.hide();
            }
        }

        // Add previous button
        Button previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDialogIndex > 0) {
                    dialogs.get(currentDialogIndex).hide(); // Hide the current dialog
                    currentDialogIndex--; // Move to the previous dialog
                    dialogs.get(currentDialogIndex).show(); // Show the previous dialog
                }
            }
        });

        // Add next button
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDialogIndex < dialogs.size() - 1) {
                    dialogs.get(currentDialogIndex).hide(); // Hide the current dialog
                    currentDialogIndex++; // Move to the next dialog
                    dialogs.get(currentDialogIndex).show(); // Show the next dialog
                }
            }
        });
    }




}