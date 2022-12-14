import tmdb.buildSrc.Modules

plugins {
    id("application-plugin")
}

dependencies {
    implementation(project(Modules.CORE))
    implementation(project(Modules.CORE_UI))
    implementation(project(Modules.MODEL))
    implementation(project(Modules.DOMAIN))
    implementation(project(Modules.UI_PEOPLE))
    implementation(project(Modules.UI_TVSHOWS))
    implementation(project(Modules.UI_MOVIES))
    implementation(project(Modules.UI_LOGIN))
    implementation(project(Modules.UI_PROFILE))

//    implementation(platform(tmdb.buildSrc.Libs.FireBase.bom))
//    implementation(tmdb.buildSrc.Libs.FireBase.fireBaseAuthKtx)
//    implementation(tmdb.buildSrc.Libs.FireBase.fireBaseDataBase)
}
