package com.example.ananth.statefarmapp;


import dagger.Component;

@ActivityScope
@Component(dependencies = NetworkComponent.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
