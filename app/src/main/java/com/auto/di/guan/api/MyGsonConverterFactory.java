package com.auto.di.guan.api;

import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 *    封装加密解密过程
 */
public class MyGsonConverterFactory extends Converter.Factory{
    private final Gson gson;
    public static MyGsonConverterFactory create() {
        return create(new Gson());
    }

    public static MyGsonConverterFactory create(Gson gson) {
        return new MyGsonConverterFactory(gson);
    }

    private MyGsonConverterFactory(Gson gson) {
        if (gson == null){
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

        return new MyResponseBodyConverter<>(gson,adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyRequestBodyConverter<>(gson, adapter);
    }


    final class MyRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final Gson mGson;
        private final TypeAdapter<T> adapter;
        MediaType mediaType = MediaType.parse("application/json");
        public MyRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.mGson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = null;
            Writer writer = null;
            JsonWriter jsonWriter = null;
            try{
                buffer = new Buffer();
                writer = new OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"));
                jsonWriter = mGson.newJsonWriter(writer);
                String request = value.toString();
                LogUtils.i("--------RequestBody  ",request.toString());
                jsonWriter.close();
                return RequestBody.create( mediaType, buffer.readByteString());
            }catch (Exception e){
                return null;
            }finally {
                if(null !=jsonWriter){
                    jsonWriter.close();
                }

                if(null !=writer){
                    writer.close();
                }

                if(null !=buffer){
                    buffer.close();
                }
            }

        }
    }


    public class MyResponseBodyConverter<T> implements Converter<ResponseBody,Object> {
        private final TypeAdapter<T> adapter;
        private final Gson mGson;
        public MyResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.mGson = gson;
            this.adapter = adapter;
        }
        @Override
        public Object convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = null;
            StringReader stringReader = null;
            try {
                stringReader = new StringReader(value.string());
                jsonReader = mGson.newJsonReader(stringReader);
                return adapter.read(jsonReader);
            } finally {
                if(null !=stringReader){
                    stringReader.close();
                }

                if(null !=jsonReader){
                    jsonReader.close();
                }

                if(null !=value){
                    value.close();
                }
            }
        }
    }
}
