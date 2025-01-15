package com.example.myproject.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R
        ;
import com.example.myproject.models.ImageButtonClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> arr_image;
    ArrayList<ImageButtonClass> arrObject;
    int x, y, z = 0, count = 0;
    ImageButton xButton, yButton;
    Timer buttonTimer;
    int openCardsCount = 0;
    long lastToastTime = 0;
    final long TOAST_DELAY = 1000;
    boolean isTwoCardsOpen = false;
    HashMap<Integer, Long> cardOpenTimeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        arr_image = new ArrayList<>();
        arrObject = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 7; j++) {
                arr_image.add(getResources().getIdentifier("exe" + j, "drawable", getPackageName()));
            }
        }

        Collections.shuffle(arr_image);
    }

    public void func_button(View view) {
        int id_button;

        if (view instanceof ImageButton) {
            final ImageButton imageButton = (ImageButton) view;

            if (openCardsCount >= 2) {
                if (!isTwoCardsOpen && System.currentTimeMillis() - lastToastTime > TOAST_DELAY) {
                    Toast.makeText(this, "You can only open two cards at a time", Toast.LENGTH_SHORT).show();
                    lastToastTime = System.currentTimeMillis();
                    isTwoCardsOpen = true;
                }
                return;
            }

            if ((id_button = findIdButton(view.getId())) == -1) {
                arrObject.add(new ImageButtonClass(arr_image.get(z), view.getId()));
                imageButton.setImageResource(arr_image.get(z));
                z++;
            } else {
                if (isCardOpen(view.getId())) {
                    if (System.currentTimeMillis() - lastToastTime > TOAST_DELAY) {
                        Toast.makeText(this, "This card is already open", Toast.LENGTH_SHORT).show();
                        lastToastTime = System.currentTimeMillis();
                    }
                    return;
                }
                imageButton.setImageResource(arrObject.get(id_button).getId());
                cardOpenTimeMap.put(view.getId(), System.currentTimeMillis());
            }

            openCardsCount++;

            if (count == 0) {
                x = arrObject.get(findIdButton(view.getId())).getId();
                xButton = imageButton;
                count++;
            } else {
                y = arrObject.get(findIdButton(view.getId())).getId();
                yButton = imageButton;
                count = 0;

                if (x == y && xButton != view) {
                    Toast.makeText(this, "You are the best", Toast.LENGTH_LONG).show();
                    view.setVisibility(View.GONE);
                    xButton.setVisibility(View.GONE);

                    if (buttonTimer != null) {
                        buttonTimer.cancel();
                    }

                    openCardsCount = 0;
                    isTwoCardsOpen = false;
                    cardOpenTimeMap.remove(view.getId());
                    cardOpenTimeMap.remove(xButton.getId());
                } else {
                    buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (imageButton.getDrawable() != getResources().getDrawable(R.mipmap.front)) {
                                        imageButton.setImageResource(R.mipmap.front);
                                    }
                                    if (xButton.getDrawable() != getResources().getDrawable(R.mipmap.front)) {
                                        xButton.setImageResource(R.mipmap.front);
                                    }
                                    openCardsCount = 0;
                                    isTwoCardsOpen = false;
                                    cardOpenTimeMap.remove(view.getId());
                                    cardOpenTimeMap.remove(xButton.getId());
                                }
                            });
                        }
                    }, 2000);
                }
            }
        }
    }

    private boolean isCardOpen(int cardId) {
        if (cardOpenTimeMap.containsKey(cardId)) {
            long openTime = cardOpenTimeMap.get(cardId);
            return System.currentTimeMillis() - openTime <= 2000;
        }
        return false;
    }

    private int findIdButton(int id) {
        for (int i = 0; i < arrObject.size(); i++) {
            if (arrObject.get(i).getArrId() == id) {
                return i;
            }
        }
        return -1;
    }
}