package com.cat.music.di.component;

import android.content.Context;


import com.cat.music.di.module.ServiceModule;
import com.cat.music.di.scope.ContextLife;
import com.cat.music.di.scope.PerService;
import com.cat.music.di.module.ServiceModule;
import com.cat.music.di.scope.ContextLife;
import com.cat.music.di.scope.PerService;
import com.cat.music.di.module.ServiceModule;
import com.cat.music.di.scope.ContextLife;
import com.cat.music.di.scope.PerService;

import dagger.Component;


/**
 * Created by lw on 2017/1/19.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
