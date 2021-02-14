package org.mai.dep810.rest_api_io_lesson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.*;

public class SerializationHelper<T extends Serializable> {

    Class<T> serialazationType;

    public SerializationHelper(Class<T> serialazationType) {
        this.serialazationType = serialazationType;
    }

    private Logger log = Logger.getLogger(getClass());

    ObjectMapper mapper = new ObjectMapper();


    /*
      Необходимо десереализовать объект из файла по указанному пути
     */
    public T loadFromFile(String path) {
        T res = null;
        try (InputStream in = new FileInputStream(path)) {
            ObjectMapper mapper = new ObjectMapper();
            res = mapper.readValue(in, serialazationType);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    /*
      Необходимо сохранить сереализованный объект в файл по указанному пути
     */
    public boolean saveToFile(String path, T toSave) {
        boolean res = false;
        // "try() - try with resources" в конструктор передается объект Closeable/Flushable
        // "So if you use the try-with statement your code gets a lot cleaner and most importantly: resource will always be closed
        // вместо того, чтобы городить finally/try блоки для close() и соотв проверки на null
        try(OutputStream out = new FileOutputStream(path)) {
            writeJsonToStream(out, toSave);
            res = true;
        }
        catch (IOException ignored) {
        }
        return res;
    }

    public String convertToJsonString(T toConvert) {
        try {
            String json = mapper.writeValueAsString(toConvert);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeJsonToStream(OutputStream output, T toWrite) {
        try {
            mapper.writeValue(output, toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public T parseJson(String json) {
        try {
            return mapper.readValue(json, serialazationType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
