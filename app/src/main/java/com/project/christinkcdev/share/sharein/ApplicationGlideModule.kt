package com.project.christinkcdev.share.sharein

import android.content.Context
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.Glide
import android.content.pm.ApplicationInfo
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.ModelLoader
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.signature.ObjectKey
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options

@GlideModule
class ApplicationGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.append(
            ApplicationInfo::class.java,
            Drawable::class.java,
            AppIconModelLoaderFactory(context)
        )
    }

    internal class AppIconDataFetcher(val context: Context, val model: ApplicationInfo) : DataFetcher<Drawable> {
        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Drawable>) {
            callback.onDataReady(context.packageManager.getApplicationIcon(model))
        }

        override fun cleanup() {
            // Empty Implementation
        }

        override fun cancel() {
            // Empty Implementation
        }

        override fun getDataClass(): Class<Drawable> {
            return Drawable::class.java
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }
    }

    internal class AppIconModelLoader(private val context: Context) : ModelLoader<ApplicationInfo, Drawable> {
        override fun buildLoadData(
            applicationInfo: ApplicationInfo, width: Int, height: Int, options: Options,
        ): LoadData<Drawable> {
            return LoadData(ObjectKey(applicationInfo), AppIconDataFetcher(context, applicationInfo))
        }

        override fun handles(applicationInfo: ApplicationInfo): Boolean {
            return true
        }
    }

    internal class AppIconModelLoaderFactory(
        private val context: Context,
    ) : ModelLoaderFactory<ApplicationInfo, Drawable> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ApplicationInfo, Drawable> {
            return AppIconModelLoader(context)
        }

        override fun teardown() {
            // Empty Implementation.
        }
    }
}