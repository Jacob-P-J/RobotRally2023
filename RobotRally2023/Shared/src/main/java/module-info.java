module shared {

    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.annotation;
    requires com.google.common;
    requires java.sql;
    requires com.google.gson;

    exports com.dtu.shared;
    exports com.dtu.shared.observer;
    exports com.dtu.shared.model;
    exports com.dtu.shared.model.fileaccess;
    exports com.dtu.shared.controller;
    exports com.dtu.shared.model.fieldTypes;
    exports com.dtu.shared.model.fileaccess.Templates;
}