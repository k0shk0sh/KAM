package com.fast.access.kam.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.access.kam.R;

/**
 * Created by Kosh on 8/20/2015. copyrights are reserved
 */
public class MemberFragment extends Fragment {

    private boolean isDeveloper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isDeveloper) {
            return inflater.inflate(R.layout.memers_layout_kosh, container, false);
        }
        return inflater.inflate(R.layout.memers_layout_aimen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.devAccount).setOnClickListener(openAcc);
        view.findViewById(R.id.devImg).setOnClickListener(openAcc);
    }

    private View.OnClickListener openAcc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isDeveloper) {
                openGPlus("+KoShKoSh");
            } else {
                openGPlus("102969874755837473287");
            }
        }
    };

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    private void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + profile + "/posts")));
        }
    }

}
