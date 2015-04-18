package kosen.talha.viagogo.com.viagogo.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

import kosen.talha.viagogo.com.viagogo.R;
import kosen.talha.viagogo.com.viagogo.adapters.TransitionAdapter;
import kosen.talha.viagogo.com.viagogo.core.Constants;
import kosen.talha.viagogo.com.viagogo.core.ViagogoApplication;
import kosen.talha.viagogo.com.viagogo.exception.LocationNotFoundException;
import kosen.talha.viagogo.com.viagogo.exception.NoBorderException;
import kosen.talha.viagogo.com.viagogo.pojos.CountryModal;
import kosen.talha.viagogo.com.viagogo.utils.WindowCompatUtils;

public class DetailActivity extends ActionBarActivity {
    private static final String EXTRA_COUNTRY = "kosen.talha.viagogo.com.viagogo.pojos.country";
    private Toolbar toolbar;
    private CountryModal countryData;
    private GoogleMap mMap;
    private ImageView image;
    private Button fab, region, clear;
    private TextView title, nativename, capital, population, demonym, area, gini, timezones, callingcodes, domain, languages;
    private LinearLayout bordersContainer;

    public static void navigate(ActionBarActivity activity, View transitionImage, CountryModal viewModel) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_COUNTRY, viewModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, String.format(Constants.FLAG_URL, viewModel.getAlpha2Code().toLowerCase()));
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_detail);
        ActivityCompat.postponeEnterTransition(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countryData = getIntent().getParcelableExtra(EXTRA_COUNTRY);
        setTitle(countryData.getName());

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map)).getMap();
        fab = (Button) findViewById(R.id.fab);
        title = (TextView) findViewById(R.id.title);
        nativename = (TextView) findViewById(R.id.nativename);
        capital = (TextView) findViewById(R.id.capital);
        population = (TextView) findViewById(R.id.population);
        demonym = (TextView) findViewById(R.id.demonym);
        area = (TextView) findViewById(R.id.area);
        gini = (TextView) findViewById(R.id.gini);
        timezones = (TextView) findViewById(R.id.timezones);
        callingcodes = (TextView) findViewById(R.id.callingcodes);
        domain = (TextView) findViewById(R.id.domain);
        languages = (TextView) findViewById(R.id.languages);
        bordersContainer = (LinearLayout) findViewById(R.id.borders);
        region = (Button) findViewById(R.id.region);
        clear = (Button) findViewById(R.id.clear);

        loadCountryFlagToImageView();
        loadCountryInformation();
        loadRegionView();


        try {
            loadNeighboringCountries(countryData.getBorders());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoBorderException e) {
            e.printStackTrace();
        }

        try {
            loadCountryLocationToMap(countryData.getLatlng(), countryData.getLocalizedName(), 4.0f);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (LocationNotFoundException e) {
            e.printStackTrace();
        }

        loadDataToView(countryData.getTimezones(), timezones);
        loadDataToView(countryData.getCallingCodes(), callingcodes);
        loadDataToView(countryData.getTopLevelDomain(), domain);
        loadDataToView(countryData.getLanguages(), languages);

    }

    private void loadCountryInformation() {
        fab.setText(countryData.getAlpha2Code());
        nativename.setText(countryData.getNativeName());
        title.setText(countryData.getName());
        capital.setText(countryData.getCapital());
        population.setText(countryData.getPopulation().toString());
        demonym.setText(countryData.getDemonym().toString());
        area.setText(countryData.getArea().toString());
        gini.setText(countryData.getGini().toString());
    }

    private void loadRegionView() {
        region.setText("Show all in " + countryData.getRegion());
        clear.setText("Show " + countryData.getLocalizedName() + " on map");

        if (mMap == null)
            return;

        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (final Map.Entry entry : ViagogoApplication.getInstance().getCountryMap().entrySet()) {
                            if (((CountryModal) entry.getValue()).getRegion().equalsIgnoreCase(countryData.getRegion())) {
                                DetailActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            loadCountryLocationToMap(((CountryModal) entry.getValue()).getLatlng(), ((CountryModal) entry.getValue()).getLocalizedName(), 1.0f);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (LocationNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                });
                            }
                        }
                    }
                });
                thread.start();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                try {
                    loadCountryLocationToMap(countryData.getLatlng(), countryData.getLocalizedName(), 4.0f);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (LocationNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNeighboringCountries(String data) throws JSONException, NoBorderException {
        final JSONArray arrBorders = new JSONArray(data);

        if (arrBorders.length() == 0)
            throw new NoBorderException(countryData.getName() + " is a island");

        addCountriesToBordersLayout(arrBorders);

    }

    private void addCountriesToBordersLayout(JSONArray arrBorders) throws JSONException {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 2, 0, 2);

        bordersContainer.removeAllViews();
        for (int i = 0; i < arrBorders.length(); i++) {
            final CountryModal modal = ViagogoApplication.getInstance().getCountryMap().get(arrBorders.get(i).toString());
            final Button btn = (Button) getLayoutInflater().inflate(R.layout.border_button, null);
            btn.setText(modal.getLocalizedName());
            btn.setLayoutParams(params);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.navigate(DetailActivity.this, v, modal);
                }
            });

            bordersContainer.addView(btn);
        }
    }

    private void loadCountryFlagToImageView() {
        image = (ImageView) findViewById(R.id.image);
        ViewCompat.setTransitionName(image, String.format(Constants.FLAG_URL, countryData.getAlpha2Code().toLowerCase()));
        Picasso.with(this).load(String.format(Constants.FLAG_URL, countryData.getAlpha2Code().toLowerCase())).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette, image);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    private void loadCountryLocationToMap(String data, String countryName, float zoom) throws JSONException, LocationNotFoundException {
        JSONArray latlng = new JSONArray(data);
        if (latlng.length() == 0)
            throw new LocationNotFoundException("There are no location information for  " + countryName);

        if (mMap == null)
            return;

        mMap.addMarker(new MarkerOptions().position(new LatLng(latlng.getDouble(0), latlng.getDouble(1))).title(countryName));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlng.getDouble(0), latlng.getDouble(1)), zoom));
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void loadDataToView(String data, TextView timezones) {
        try {
            JSONArray array = new JSONArray(data);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length(); i++) {

                sb.append(array.get(i));
                if (i < array.length() - 1)
                    sb.append(", ");

                array.put(i, null);
            }

            timezones.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // methods for material desing, can be found on any blogs
    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette, ImageView image) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        toolbar.setBackgroundColor(palette.getMutedColor(primary));
        WindowCompatUtils.setStatusBarcolor(getWindow(), palette.getDarkMutedColor(primaryDark));
        initScrollFade(image);
        ActivityCompat.startPostponedEnterTransition(this);
    }

    private void initScrollFade(final ImageView image) {
        final View scrollView = findViewById(R.id.scroll);

        setComponentsStatus(scrollView, image);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setComponentsStatus(scrollView, image);
            }
        });
    }

    private void setComponentsStatus(View scrollView, ImageView image) {
        int scrollY = scrollView.getScrollY();
        if (Build.VERSION.SDK_INT > 10)
            image.setTranslationY(-scrollY / 2);

        ColorDrawable background = (ColorDrawable) toolbar.getBackground();
        int padding = scrollView.getPaddingTop();
        double alpha = (1 - (((double) padding - (double) scrollY) / (double) padding)) * 255.0;
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;

        background.setAlpha((int) alpha);

        float scrollRatio = (float) (alpha / 255f);
        int titleColor = getAlphaColor(Color.WHITE, scrollRatio);
        toolbar.setTitleTextColor(titleColor);
    }

    private int getAlphaColor(int color, float scrollRatio) {
        return Color.argb((int) (scrollRatio * 255f), Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * It seems that the ActionBar view is reused between activities. Changes need to be reverted,
     * or the ActionBar will be transparent when we go back to Main Activity
     */
    private void restablishActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getReturnTransition().addListener(new TransitionAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    toolbar.setTitleTextColor(Color.WHITE);
                    toolbar.getBackground().setAlpha(255);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        restablishActionBar();
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            restablishActionBar();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMap != null) {
            mMap.clear();
            mMap = null;
        }

        countryData = null;
    }

}
