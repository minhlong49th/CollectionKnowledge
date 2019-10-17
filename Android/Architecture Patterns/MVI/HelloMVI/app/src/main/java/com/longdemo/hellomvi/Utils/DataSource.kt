package com.longdemo.hellomvi.Utils

import io.reactivex.Observable

class DataSource(internal var imagelist: List<String>)
{
    fun getImageLinkFromList(index: Int): Observable<String>
    {
        return Observable.just(imagelist[index])
    }
}