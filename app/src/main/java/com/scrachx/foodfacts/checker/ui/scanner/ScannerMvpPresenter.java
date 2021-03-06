package com.scrachx.foodfacts.checker.ui.scanner;

import com.scrachx.foodfacts.checker.data.network.model.Product;
import com.scrachx.foodfacts.checker.di.PerActivity;
import com.scrachx.foodfacts.checker.ui.base.MvpPresenter;

/**
 * Created by scots on 07/05/2017.
 */

@PerActivity
public interface ScannerMvpPresenter <V extends ScannerMvpView> extends MvpPresenter<V> {

    void loadProduct(String barcode);

    void saveProduct(Product product);

}
