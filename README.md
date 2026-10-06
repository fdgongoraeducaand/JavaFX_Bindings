Resumen de Patrones y Usos de Propiedades y Bindings Demostrados
1. Propiedades Observables (Property<T>)
Son los contenedores reactivos básicos de JavaFX (SimpleStringProperty, SimpleDoubleProperty, SimpleBooleanProperty, etc.). Permiten envolver valores para que la interfaz o la lógica de negocio puedan escuchar sus cambios de forma automática.
2. Enlace Unidireccional (bind) y Bidireccional (bindBidirectional)
Unidireccional (bind): Establece una relación maestro-esclavo en una sola dirección.
Ejemplo: El progreso de un ProgressBar o ProgressIndicator se enlaza directamente a la propiedad del Slider dividida entre 100:
progressBar.progressProperty().bind(slider.valueProperty().divide(100.0));
Bidireccional (bindBidirectional): Sincroniza dos propiedades de forma simétrica. Editar cualquiera de las dos actualiza instantáneamente a la otra.
Ejemplo: Dos campos de texto TextField o un Slider y un Spinner numérico:
txtCampoA.textProperty().bindBidirectional(txtCampoB.textProperty());
3. API Fluida y Métodos de Calculado (Bindings)
Permite realizar operaciones matemáticas, concatenación de cadenas y condiciones lógicas directamente sobre las propiedades:
Operaciones Matemáticas:
DoubleBinding subtotalBinding = cantidad.multiply(precioUnitario);
DoubleBinding ivaBinding = subtotalBinding.multiply(0.21);
DoubleBinding totalBinding = subtotalBinding.add(ivaBinding);
Condicionales (Ternarios Bindings.when):
StringBinding estadoCuenta = Bindings.when(saldo.greaterThanOrEqualTo(0.0))
    .then("🟢 Cuenta Solvente")
    .otherwise("🔴 En Números Rojos");
4. Bindings Personalizados de Bajo Nivel (createBooleanBinding)
Cuando la lógica de validación abarca múltiples campos o reglas complejas, se utiliza Bindings.createBooleanBinding() especificando las propiedades de las que depende:
BooleanBinding formularioInvalido = Bindings.createBooleanBinding(() -> {
    boolean nombreOk = nombreUsuario.get().trim().length() >= 3;
    boolean emailOk = emailUsuario.get().contains("@");
    boolean acepta = chkAceptaTerminos.isSelected();
    return !(nombreOk && emailOk && acepta);
}, nombreUsuario, emailUsuario, chkAceptaTerminos.selectedProperty());

// Deshabilita el botón de envío automáticamente mientras el formulario sea inválido
btnEnviar.disableProperty().bind(formularioInvalido);
5. Colecciones Observables y Diferencia entre Listeners
Bindings.size(ObservableList): Enlaza el número de elementos de una lista con controles o etiquetas en tiempo real.
ChangeListener vs InvalidationListener:
ChangeListener: Se dispara cuando el valor real ha cambiado y calcula el valor anterior (oldVal) y el nuevo (newVal).
InvalidationListener: Se dispara de forma perezosa (lazy evaluation) en cuanto la propiedad se marca como inválida, sin forzar la reevaluación del valor hasta que sea requerido.
