package me.earth.earthhack.impl.util.mcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MappingProvider
{
    private static final Map<String, String> FIELD_MAPPINGS;

    static
    {
        int fs = (int) (11915 / 0.75f); // amount of fields / loadfactor
        FIELD_MAPPINGS = new HashMap<>(fs);

        try (BufferedReader br =
             new BufferedReader(
                 new InputStreamReader(
                     Objects.requireNonNull(
                         MappingProvider
                             .class
                             .getClassLoader()
                             .getResourceAsStream("mappings/mappings.csv")
                     )
                 )
             ))
        {
            String line;
            while ((line = br.readLine()) != null) {
                String[] mapping = line.replace("/", ".").split(",");

                if (mapping.length > 0)
                    if (mapping[0].equals("field"))
                        FIELD_MAPPINGS.put(mapping[2], mapping[3]);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String field(String fieldName)
    {
        return FIELD_MAPPINGS.get(fieldName);
    }

    public static String simpleName(Class<?> clazz)
    {
        return clazz.getSimpleName();
    }

}
