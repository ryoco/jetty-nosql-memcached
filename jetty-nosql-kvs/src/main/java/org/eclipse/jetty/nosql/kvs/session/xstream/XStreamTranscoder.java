package org.eclipse.jetty.nosql.kvs.session.xstream;

import org.eclipse.jetty.nosql.kvs.session.ISerializationTranscoder;
import org.eclipse.jetty.nosql.kvs.session.TranscoderException;

import com.thoughtworks.xstream.XStream;

public class XStreamTranscoder implements ISerializationTranscoder {
  private XStream xstream = null;
  private ClassLoader classLoader = null;

  public XStreamTranscoder() {
    this(Thread.currentThread().getContextClassLoader());
  }

  public XStreamTranscoder(ClassLoader cl) {
    xstream = new XStream();
// FIXME: using SessionManager's `getContext().getClassLoader()` causes `com.thoughtworks.xstream.mapper.CannotResolveClassException`
//        on deserializing `org.eclipse.jetty.nosql.kvs.session.xstream.XStreamSession`
//  xstream.setClassLoader(cl);
    classLoader = cl;
  }

  public byte[] encode(Object obj) throws TranscoderException {
    byte[] raw = null;
    try {
      raw = xstream.toXML(obj).getBytes("ISO-8859-1");
    } catch (Exception error) {
      throw(new TranscoderException(error));
    }
    return raw;
  }

  @SuppressWarnings("unchecked")
  public <T> T decode(byte[] raw, Class<T> klass) throws TranscoderException {
    T obj = null;
    try {
      obj = (T) xstream.fromXML(new String(raw, "ISO-8859-1"));
    } catch (Exception error) {
      throw(new TranscoderException(error));
    }
    return obj;
  }
}
