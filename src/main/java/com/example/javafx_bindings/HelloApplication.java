package com.example.javafx_bindings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    // Modelo de datos con Propiedades Observables
    private final StringProperty nombreUsuario = new SimpleStringProperty("");
    private final StringProperty emailUsuario = new SimpleStringProperty("");
    private final DoubleProperty cantidad = new SimpleDoubleProperty(1.0);
    private final DoubleProperty precioUnitario = new SimpleDoubleProperty(25.50);
    private final DoubleProperty saldoCuenta = new SimpleDoubleProperty(100.0);
    private final ObservableList<String> listaTareas = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                crearPestanaUniBidireccional(),
                crearPestanaApiFluida(),
                crearPestanaBindingsPersonalizados(),
                crearPestanaColeccionesYListeners()
        );

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Barra de estado inferior con binding global
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(8, 15, 8, 15));
        statusBar.setStyle("-fx-background-color: #1e1e2e; -fx-border-color: #313244; -fx-border-width: 1 0 0 0;");
        Label lblEstadoGlobal = new Label();
        lblEstadoGlobal.setStyle("-fx-text-fill: #a6e3a1; -fx-font-weight: bold;");

        // Binding del estado global que reacciona a la lista y campos
        lblEstadoGlobal.textProperty().bind(
                Bindings.concat("⚡ Demostración JavaFX | Tareas en lista: ", Bindings.size(listaTareas).asString())
        );
        statusBar.getChildren().add(lblEstadoGlobal);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 850, 600);

        // Estilos CSS modernos integrados
        scene.getStylesheets().add("data:text/css," +
                ".root { -fx-base: #1e1e2e; -fx-background-color: #1e1e2e; -fx-font-family: 'Segoe UI', sans-serif; }" +
                ".tab-header-background { -fx-background-color: #181825; }" +
                ".tab { -fx-background-color: #313244; -fx-padding: 8 16; }" +
                ".tab:selected { -fx-background-color: #89b4fa; }" +
                ".tab:selected .tab-label { -fx-text-fill: #11111b; -fx-font-weight: bold; }" +
                ".tab-label { -fx-text-fill: #cdd6f4; }" +
                ".card { -fx-background-color: #313244; -fx-background-radius: 8; -fx-padding: 15; }" +
                ".title { -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #89b4fa; }" +
                ".label { -fx-text-fill: #cdd6f4; }" +
                ".text-field, .spinner { -fx-background-color: #181825; -fx-text-fill: #cdd6f4; -fx-border-color: #45475a; -fx-border-radius: 4; }" +
                ".button { -fx-background-color: #89b4fa; -fx-text-fill: #11111b; -fx-font-weight: bold; -fx-background-radius: 4; }" +
                ".button:disabled { -fx-background-color: #45475a; -fx-text-fill: #6c7086; }"
        );

        primaryStage.setTitle("Demostración Completa de Propiedades y Bindings en JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    // =========================================================================
    // PESTAÑA 1: Enlaces Unidireccionales y Bidireccionales
    // =========================================================================
    private Tab crearPestanaUniBidireccional() {
        Tab tab = new Tab("1. Uni & Bidireccional");
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(20));

        // --- SECCIÓN A: Enlace Unidireccional (bind) ---
        VBox cardUni = new VBox(10);
        cardUni.getStyleClass().add("card");
        Label lblTituloUni = new Label("A) Enlace Unidireccional (bind)");
        lblTituloUni.getStyleClass().add("title");

        Slider sliderProgress = new Slider(0, 100, 45);
        ProgressBar progressBar = new ProgressBar();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label lblValorSlider = new Label();

        // 1. Unidireccional: ProgressBar <- Slider (dividido entre 100)
        progressBar.progressProperty().bind(sliderProgress.valueProperty().divide(100.0));
        progressIndicator.progressProperty().bind(sliderProgress.valueProperty().divide(100.0));

        // 2. Unidireccional con formato de texto: Label <- Slider
        lblValorSlider.textProperty().bind(
                Bindings.format("Valor actual: %.1f %%", sliderProgress.valueProperty())
        );

        HBox boxProgress = new HBox(15, progressBar, progressIndicator, lblValorSlider);
        boxProgress.setAlignment(Pos.CENTER_LEFT);
        cardUni.getChildren().addAll(lblTituloUni, new Label("Mueve el Slider para actualizar los controles:"), sliderProgress, boxProgress);

        // --- SECCIÓN B: Enlace Bidireccional (bindBidirectional) ---
        VBox cardBidi = new VBox(10);
        cardBidi.getStyleClass().add("card");
        Label lblTituloBidi = new Label("B) Enlace Bidireccional (bindBidirectional)");
        lblTituloBidi.getStyleClass().add("title");

        TextField txtCampoA = new TextField("Texto sincronizado");
        TextField txtCampoB = new TextField();

        // 3. Bidireccional entre dos campos de texto (editar cualquiera actualiza el otro)
        txtCampoA.textProperty().bindBidirectional(txtCampoB.textProperty());

        Slider sliderBidi = new Slider(0, 100, 25);
        Spinner<Double> spinnerBidi = new Spinner<>(0.0, 100.0, 25.0, 1.0);
        spinnerBidi.setEditable(true);

        // 4. Bidireccional entre Slider y Spinner (vía ObjectProperty<Double> del Spinner)
        sliderBidi.valueProperty().bindBidirectional(
                (Property<Number>) (Property<?>) spinnerBidi.getValueFactory().valueProperty()
        );

        GridPane gridBidi = new GridPane();
        gridBidi.setHgap(10); gridBidi.setVgap(10);
        gridBidi.add(new Label("Campo A:"), 0, 0); gridBidi.add(txtCampoA, 1, 0);
        gridBidi.add(new Label("Campo B (Sincronizado):"), 0, 1); gridBidi.add(txtCampoB, 1, 1);
        gridBidi.add(new Label("Slider:"), 0, 2); gridBidi.add(sliderBidi, 1, 2);
        gridBidi.add(new Label("Spinner (Sincronizado):"), 0, 3); gridBidi.add(spinnerBidi, 1, 3);

        cardBidi.getChildren().addAll(lblTituloBidi, gridBidi);

        contenedor.getChildren().addAll(cardUni, cardBidi);
        tab.setContent(new ScrollPane(contenedor));
        return tab;
    }

    // =========================================================================
    // PESTAÑA 2: API Fluida y Clase de Utilidad Bindings
    // =========================================================================
    private Tab crearPestanaApiFluida() {
        Tab tab = new Tab("2. API Fluida & Calculados");
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(20));

        // --- SECCIÓN A: Operaciones Matemáticas Fluidas ---
        VBox cardMath = new VBox(12);
        cardMath.getStyleClass().add("card");
        Label lblTituloMath = new Label("A) Cálculo Dinámico de Total (API Fluida)");
        lblTituloMath.getStyleClass().add("title");

        TextField txtCantidad = new TextField("2");
        TextField txtPrecio = new TextField("49.99");
        Label lblSubtotal = new Label();
        Label lblIVA = new Label();
        Label lblTotalConIVA = new Label();

        // Actualizamos las propiedades del modelo desde los TextFields
        txtCantidad.textProperty().addListener((obs, oldV, newV) -> {
            try { cantidad.set(Double.parseDouble(newV)); } catch (NumberFormatException ignored) {}
        });
        txtPrecio.textProperty().addListener((obs, oldV, newV) -> {
            try { precioUnitario.set(Double.parseDouble(newV)); } catch (NumberFormatException ignored) {}
        });

        // 1. API Fluida: Subtotal = Cantidad * Precio
        DoubleBinding subtotalBinding = cantidad.multiply(precioUnitario);

        // 2. API Fluida: IVA (21%) = Subtotal * 0.21
        DoubleBinding ivaBinding = subtotalBinding.multiply(0.21);

        // 3. API Fluida: Total = Subtotal + IVA
        DoubleBinding totalBinding = subtotalBinding.add(ivaBinding);

        // Enlace visual con formato
        lblSubtotal.textProperty().bind(Bindings.format("Subtotal: %.2f €", subtotalBinding));
        lblIVA.textProperty().bind(Bindings.format("IVA (21%%): %.2f €", ivaBinding));
        lblTotalConIVA.textProperty().bind(Bindings.format("TOTAL: %.2f €", totalBinding));
        lblTotalConIVA.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #a6e3a1;");

        GridPane gridMath = new GridPane();
        gridMath.setHgap(10); gridMath.setVgap(8);
        gridMath.add(new Label("Cantidad:"), 0, 0); gridMath.add(txtCantidad, 1, 0);
        gridMath.add(new Label("Precio Unitario (€):"), 0, 1); gridMath.add(txtPrecio, 1, 1);
        gridMath.add(lblSubtotal, 0, 2, 2, 1);
        gridMath.add(lblIVA, 0, 3, 2, 1);
        gridMath.add(lblTotalConIVA, 0, 4, 2, 1);

        cardMath.getChildren().addAll(lblTituloMath, gridMath);

        // --- SECCIÓN B: Bindings Condicionales (Ternarios) ---
        VBox cardCondicional = new VBox(12);
        cardCondicional.getStyleClass().add("card");
        Label lblTituloCond = new Label("B) Binding Condicional (Bindings.when)");
        lblTituloCond.getStyleClass().add("title");

        Slider sliderSaldo = new Slider(-50, 150, 100);
        saldoCuenta.bind(sliderSaldo.valueProperty());

        Label lblEstadoSaldo = new Label();

        // Binding Condicional: Ternario (If-Then-Else)
        StringBinding estadoCuentaBinding = Bindings.when(saldoCuenta.greaterThanOrEqualTo(0.0))
                .then(Bindings.concat("🟢 Cuenta Solvente (Saldo: ", saldoCuenta.asString("%.2f €"), ")"))
                .otherwise(Bindings.concat("🔴 En Números Rojos (Deuda: ", saldoCuenta.asString("%.2f €"), ")"));

        lblEstadoSaldo.textProperty().bind(estadoCuentaBinding);

        cardCondicional.getChildren().addAll(lblTituloCond, new Label("Ajusta el saldo de la cuenta:"), sliderSaldo, lblEstadoSaldo);

        contenedor.getChildren().addAll(cardMath, cardCondicional);
        tab.setContent(new ScrollPane(contenedor));
        return tab;
    }

    // =========================================================================
    // PESTAÑA 3: Bindings Personalizados y Validación
    // =========================================================================
    private Tab crearPestanaBindingsPersonalizados() {
        Tab tab = new Tab("3. Validación Personalizada");
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(20));

        VBox cardForm = new VBox(15);
        cardForm.getStyleClass().add("card");
        Label lblTituloForm = new Label("Validación de Formulario (createBooleanBinding)");
        lblTituloForm.getStyleClass().add("title");

        TextField txtNombreForm = new TextField();
        TextField txtEmailForm = new TextField();
        CheckBox chkAceptaTerminos = new CheckBox("Acepto los términos y condiciones del servicio");
        Button btnEnviarForm = new Button("Enviar Formulario");

        // Conectamos con las propiedades
        nombreUsuario.bind(txtNombreForm.textProperty());
        emailUsuario.bind(txtEmailForm.textProperty());

        // --- BINDING PERSONALIZADO DE BAJO NIVEL ---
        // Se recalcula automáticamente cuando cambia nombreUsuario, emailUsuario o chkAceptaTerminos
        BooleanBinding formularioInvalido = Bindings.createBooleanBinding(() -> {
            String nom = nombreUsuario.get().trim();
            String mail = emailUsuario.get().trim();
            boolean acepta = chkAceptaTerminos.isSelected();

            boolean nombreOk = !nom.isEmpty() && nom.length() >= 3;
            boolean emailOk = mail.contains("@") && mail.contains(".");

            return !(nombreOk && emailOk && acepta);
        }, nombreUsuario, emailUsuario, chkAceptaTerminos.selectedProperty());

        // Enlazamos la propiedad 'disable' del botón al estado de invalidez
        btnEnviarForm.disableProperty().bind(formularioInvalido);

        // Feedback de errores dinámico mediante StringBinding
        Label lblMensajeError = new Label();
        lblMensajeError.textProperty().bind(Bindings.when(formularioInvalido)
                .then("❌ El formulario contiene errores (Nombre >= 3 car., Email válido y Términos aceptados).")
                .otherwise("✅ Formulario válido y listo para enviar."));

        lblMensajeError.styleProperty().bind(Bindings.when(formularioInvalido)
                .then("-fx-text-fill: #f38ba8;")
                .otherwise("-fx-text-fill: #a6e3a1;"));

        GridPane gridForm = new GridPane();
        gridForm.setHgap(10); gridForm.setVgap(10);
        gridForm.add(new Label("Nombre completo:"), 0, 0); gridForm.add(txtNombreForm, 1, 0);
        gridForm.add(new Label("Correo electrónico:"), 0, 1); gridForm.add(txtEmailForm, 1, 1);
        gridForm.add(chkAceptaTerminos, 1, 2);
        gridForm.add(btnEnviarForm, 1, 3);
        gridForm.add(lblMensajeError, 0, 4, 2, 1);

        cardForm.getChildren().addAll(lblTituloForm, gridForm);
        contenedor.getChildren().add(cardForm);
        tab.setContent(new ScrollPane(contenedor));
        return tab;
    }

    // =========================================================================
    // PESTAÑA 4: Colecciones Observables y Listeners
    // =========================================================================
    private Tab crearPestanaColeccionesYListeners() {
        Tab tab = new Tab("4. Colecciones & Listeners");
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(20));

        // --- SECCIÓN A: Bindings sobre Colecciones Observables ---
        VBox cardList = new VBox(12);
        cardList.getStyleClass().add("card");
        Label lblTituloList = new Label("A) Binding sobre ObservableList");
        lblTituloList.getStyleClass().add("title");

        TextField txtNuevaTarea = new TextField();
        txtNuevaTarea.setPromptText("Escriba una nueva tarea...");
        Button btnAgregarTarea = new Button("Agregar Tarea");
        Button btnEliminarTarea = new Button("Eliminar Seleccionada");

        ListView<String> listViewTareas = new ListView<>(listaTareas);
        listViewTareas.setPrefHeight(120);

        btnAgregarTarea.setOnAction(e -> {
            if (!txtNuevaTarea.getText().trim().isEmpty()) {
                listaTareas.add(txtNuevaTarea.getText().trim());
                txtNuevaTarea.clear();
            }
        });

        btnEliminarTarea.setOnAction(e -> {
            String sel = listViewTareas.getSelectionModel().getSelectedItem();
            if (sel != null) {
                listaTareas.remove(sel);
            }
        });

        // Binding de la propiedad disabled al selector de la lista
        btnEliminarTarea.disableProperty().bind(listViewTareas.getSelectionModel().selectedItemProperty().isNull());

        Label lblConteoTareas = new Label();
        // Binding directo del tamaño de la lista observable
        lblConteoTareas.textProperty().bind(
                Bindings.concat("Total de tareas pendientes: ", Bindings.size(listaTareas))
        );

        HBox boxInput = new HBox(10, txtNuevaTarea, btnAgregarTarea, btnEliminarTarea);
        cardList.getChildren().addAll(lblTituloList, boxInput, listViewTareas, lblConteoTareas);

        // --- SECCIÓN B: ChangeListener vs InvalidationListener ---
        VBox cardListeners = new VBox(12);
        cardListeners.getStyleClass().add("card");
        Label lblTituloListeners = new Label("B) ChangeListener vs InvalidationListener");
        lblTituloListeners.getStyleClass().add("title");

        Slider sliderListener = new Slider(0, 100, 50);
        TextArea txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.setPrefHeight(120);

        // 1. ChangeListener: Se ejecuta cuando se calcula y cambia el VALOR REAL (oldValue -> newValue)
        sliderListener.valueProperty().addListener((obs, oldVal, newVal) -> {
            txtLog.appendText(String.format("🔎 [ChangeListener] Valor cambió de %.1f a %.1f\n", oldVal, newVal));
        });

        // 2. InvalidationListener: Se ejecuta en cuanto la propiedad se vuelve INVÁLIDA (Lazy Evaluation)
        sliderListener.valueProperty().addListener(obs -> {
            txtLog.appendText("⚡ [InvalidationListener] La propiedad se ha marcado como inválida.\n");
        });

        Button btnLimpiarLog = new Button("Limpiar Consola");
        btnLimpiarLog.setOnAction(e -> txtLog.clear());

        cardListeners.getChildren().addAll(lblTituloListeners, new Label("Mueve el Slider para observar la diferencia entre listeners:"), sliderListener, txtLog, btnLimpiarLog);

        contenedor.getChildren().addAll(cardList, cardListeners);
        tab.setContent(new ScrollPane(contenedor));
        return tab;
    }
}
