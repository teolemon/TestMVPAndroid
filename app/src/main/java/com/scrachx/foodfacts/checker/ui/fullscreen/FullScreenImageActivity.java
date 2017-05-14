package com.scrachx.foodfacts.checker.ui.fullscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.scrachx.foodfacts.checker.R;
import com.scrachx.foodfacts.checker.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.uiOptions;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by scots on 08/05/2017.
 */

public class FullScreenImageActivity extends BaseActivity implements FullScreenImageMvpView {

    @Inject
    FullScreenImagePresenter<FullScreenImageMvpView> mPresenter;

    @BindView(R.id.image_view_full_screen)
    PhotoView mImageView;

    @BindView(R.id.title_nav_header)
    TextView mTitleNavHeaderView;

    public static Intent getStartIntent(Context context, String imageurl, String barcode) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra("imageurl", imageurl);
        intent.putExtra("barcode", barcode);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        String imageurl = intent.getStringExtra("imageurl");
        String titleProduct = intent.getStringExtra("barcode");

        if(isNotEmpty(titleProduct)) {
            mTitleNavHeaderView.setText(titleProduct);
        } else {
            mTitleNavHeaderView.setText("");
        }

        if (isNotEmpty(imageurl)) {
            Glide.with(this)
                    .load(imageurl)
                    .into(mImageView);
            fullScreen();
        }
    }

    @OnClick(R.id.nav_back_btn)
    void onNavBackClick() {
        finish();
    }

    public void fullScreen() {
        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled = isImmersiveModeEnabled();
        if (isImmersiveModeEnabled) {
            Log.i("TEST", "Turning immersive mode mode off. ");
        } else {
            Log.i("TEST", "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

    private boolean isImmersiveModeEnabled() {
        return ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
    }

}
