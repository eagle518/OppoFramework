package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class RFC2965Spec extends RFC2109Spec {
    public RFC2965Spec() {
        this(null, false);
    }

    public RFC2965Spec(String[] datepatterns, boolean oneHeader) {
        super(datepatterns, oneHeader);
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new RFC2965DomainAttributeHandler());
        registerAttribHandler(ClientCookie.PORT_ATTR, new RFC2965PortAttributeHandler());
        registerAttribHandler(ClientCookie.COMMENTURL_ATTR, new RFC2965CommentUrlAttributeHandler());
        registerAttribHandler(ClientCookie.DISCARD_ATTR, new RFC2965DiscardAttributeHandler());
        registerAttribHandler(ClientCookie.VERSION_ATTR, new RFC2965VersionAttributeHandler());
    }

    private BasicClientCookie createCookie(String name, String value, CookieOrigin origin) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setPath(CookieSpecBase.getDefaultPath(origin));
        cookie.setDomain(CookieSpecBase.getDefaultDomain(origin));
        return cookie;
    }

    private BasicClientCookie createCookie2(String name, String value, CookieOrigin origin) {
        BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
        cookie.setPath(CookieSpecBase.getDefaultPath(origin));
        cookie.setDomain(CookieSpecBase.getDefaultDomain(origin));
        cookie.setPorts(new int[]{origin.getPort()});
        return cookie;
    }

    public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null");
        } else if (origin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            origin = adjustEffectiveHost(origin);
            HeaderElement[] elems = header.getElements();
            List<Cookie> cookies = new ArrayList(elems.length);
            for (HeaderElement headerelement : elems) {
                String name = headerelement.getName();
                String value = headerelement.getValue();
                if (name == null || name.length() == 0) {
                    throw new MalformedCookieException("Cookie name may not be empty");
                }
                BasicClientCookie cookie;
                if (header.getName().equals(SM.SET_COOKIE2)) {
                    cookie = createCookie2(name, value, origin);
                } else {
                    cookie = createCookie(name, value, origin);
                }
                NameValuePair[] attribs = headerelement.getParameters();
                Map<String, NameValuePair> attribmap = new HashMap(attribs.length);
                for (int j = attribs.length - 1; j >= 0; j--) {
                    NameValuePair param = attribs[j];
                    attribmap.put(param.getName().toLowerCase(Locale.ENGLISH), param);
                }
                for (Entry<String, NameValuePair> entry : attribmap.entrySet()) {
                    NameValuePair attrib = (NameValuePair) entry.getValue();
                    String s = attrib.getName().toLowerCase(Locale.ENGLISH);
                    cookie.setAttribute(s, attrib.getValue());
                    CookieAttributeHandler handler = findAttribHandler(s);
                    if (handler != null) {
                        handler.parse(cookie, attrib.getValue());
                    }
                }
                cookies.add(cookie);
            }
            return cookies;
        }
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (origin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            super.validate(cookie, adjustEffectiveHost(origin));
        }
    }

    public boolean match(Cookie cookie, CookieOrigin origin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (origin != null) {
            return super.match(cookie, adjustEffectiveHost(origin));
        } else {
            throw new IllegalArgumentException("Cookie origin may not be null");
        }
    }

    protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
        super.formatCookieAsVer(buffer, cookie, version);
        if (cookie instanceof ClientCookie) {
            String s = ((ClientCookie) cookie).getAttribute(ClientCookie.PORT_ATTR);
            if (s != null) {
                buffer.append("; $Port");
                buffer.append("=\"");
                if (s.trim().length() > 0) {
                    int[] ports = cookie.getPorts();
                    if (ports != null) {
                        int len = ports.length;
                        for (int i = 0; i < len; i++) {
                            if (i > 0) {
                                buffer.append(",");
                            }
                            buffer.append(Integer.toString(ports[i]));
                        }
                    }
                }
                buffer.append("\"");
            }
        }
    }

    private static CookieOrigin adjustEffectiveHost(CookieOrigin origin) {
        String host = origin.getHost();
        boolean isLocalHost = true;
        for (int i = 0; i < host.length(); i++) {
            char ch = host.charAt(i);
            if (ch == '.' || ch == ':') {
                isLocalHost = false;
                break;
            }
        }
        if (isLocalHost) {
            return new CookieOrigin(host + ".local", origin.getPort(), origin.getPath(), origin.isSecure());
        }
        return origin;
    }

    public int getVersion() {
        return 1;
    }

    public Header getVersionHeader() {
        CharArrayBuffer buffer = new CharArrayBuffer(40);
        buffer.append(SM.COOKIE2);
        buffer.append(": ");
        buffer.append("$Version=");
        buffer.append(Integer.toString(getVersion()));
        return new BufferedHeader(buffer);
    }
}
