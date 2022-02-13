package com.mossonthetree.codegeneratorlib.elasticsearchdjango;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class DjangoElasticClientGenerator extends Generator {
    private String classTmpl;
    private String selectByUniqueTmpl;

    public DjangoElasticClientGenerator(Database db) {
        super(db, "./templates/elasticsearch/python");
    }

    @Override
    public Map<String, String> generate() {
        Map<String, String> files = new HashMap<>();
        for(Table t: db.getTables()) {
            if(t.isJoined() || t.isLooped()) {
                continue;
            }
            files.put(t.getName() + "_elastic_client.py", generateClient(t));
        }
        return files;
    }

    private String generateClient(Table t) {
        return classTmpl
                .replace("{searchbyuniques}", generateSelectByUnique(t))
                .replace("{tablepascal}", t.getPascalName())
                .replace("{tablename}", t.getName())
                .replace("{primaryname}", t.getPrimaryColumn().getName())
                .replace("{primarycamel}", t.getPrimaryColumn().getCamelName());
    }

    private String generateSelectByUnique(Table t) {
        StringBuilder b = new StringBuilder();
        for(Column c: t.getUniqueColumns()) {
            b
                    .append("\n\n")
                    .append(selectByUniqueTmpl
                            .replace("{colname}", c.getName()));
        }
        return b.toString();
    }

    @Override
    protected void loadTemplates() {
        classTmpl = loadTemplate("class");
        selectByUniqueTmpl = loadTemplate("selectbyunique");
    }
}
