package de.javagl.jgltf.util.android.content;


import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import de.javagl.jgltf.util.android.AndroidURLConnection;

/**
 * Android's content url handler
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(final URL url) {
        return new AndroidURLConnection(url);
    }

}