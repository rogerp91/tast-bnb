package com.github.testairbnd.util;

import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Roger Patiño on 17/02/2016.
 */
public class ProfileView {

    private TextView names;
    private CircleImageView circleImageView;

    public TextView getNames() {
        return names;
    }

    public void setNames(TextView names) {
        this.names = names;
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }
}
