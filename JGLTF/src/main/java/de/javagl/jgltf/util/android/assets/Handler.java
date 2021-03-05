package de.javagl.jgltf.util.android.assets;


import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import de.javagl.jgltf.util.android.AndroidURLConnection;

/**
 *  App's assets URL handler
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(final URL url) {
        return new AndroidURLConnection(url);
    }

}