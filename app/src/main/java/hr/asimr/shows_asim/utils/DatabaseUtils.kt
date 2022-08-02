package hr.asimr.shows_asim.utils

import android.app.Activity
import hr.asimr.shows_asim.ShowsApplication
import hr.asimr.shows_asim.dao.ShowsDatabase

fun Activity.getDatabase(): ShowsDatabase = (this.application as ShowsApplication).showsDatabase
