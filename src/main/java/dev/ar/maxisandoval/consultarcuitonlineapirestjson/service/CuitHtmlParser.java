package dev.ar.maxisandoval.consultarcuitonlineapirestjson.service;

import dev.ar.maxisandoval.consultarcuitonlineapirestjson.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class CuitHtmlParser {

    public static CuitResultDto parse(String html) {
        Document d = Jsoup.parse(html);

        String query = textOrNull(d, ".search-query h1");
        String nombre = textOrNull(d, "h2.denominacion");
        String cuitFmt = textOrNull(d, ".linea-cuit-persona .cuit");

        String tipoPersona = null;
        String sexo = null;

        Element docFacets = d.selectFirst(".doc-facets");
        if (docFacets != null) {
            String text = docFacets.text();
            int idxPersona = text.indexOf("Persona");
            if (idxPersona >= 0) {
                String sub = text.substring(idxPersona);
                int par = sub.indexOf('(');
                if (par > 0) {
                    tipoPersona = sub.substring(0, par).trim();
                    int close = sub.indexOf(')', par);
                    if (close > 0) sexo = sub.substring(par + 1, close).trim();
                } else tipoPersona = sub.trim();
            }
        }

        return new CuitResultDto(
                nullToEmpty(query),
                nullToEmpty(nombre),
                nullToEmpty(cuitFmt),
                nullToEmpty(tipoPersona),
                nullToEmpty(sexo)
        );
    }

    private static String textOrNull(Element root, String css) {
        if (root == null) return null;
        Element e = root.selectFirst(css);
        return e != null ? e.text() : null;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}