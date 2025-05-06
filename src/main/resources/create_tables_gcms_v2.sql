
-- derivatization methods
CREATE TABLE derivatization_methods (
  derivatization_id int AUTO_INCREMENT PRIMARY KEY,
  derivatization_type varchar(255)
);

-- tabla compounds ya esta creada
-- creates n-m table (compound-derivatization)
CREATE TABLE compounds_derivatizationmethods(
  compound_id int NOT NULL,
  derivatization_method_id int NOT NULL,
  PRIMARY KEY (compound_id, derivatization_method_id),
  FOREIGN KEY (compound_id) REFERENCES compounds(compound_id) ON DELETE CASCADE,
  FOREIGN KEY (derivatization_method_id) REFERENCES derivatization_methods(derivatization_id) ON DELETE CASCADE
);

-- creates table gcms spectrum (in relationship with the n-m table compounds-derivatization)
CREATE TABLE gcms_spectrum (
  gcms_spectrum_id INT AUTO_INCREMENT PRIMARY KEY,
  compound_id INT NOT NULL,
  derivatization_id INT NOT NULL,
  FOREIGN KEY (compound_id) REFERENCES compounds_derivatizationmethods(compound_id) ON DELETE CASCADE,
  FOREIGN KEY (derivatization_id) REFERENCES compounds_derivatizationmethods(derivatization_method_id) ON DELETE CASCADE
);

-- creates table gcms peak
CREATE TABLE gcms_peaks (
  gcms_peaks_id INT AUTO_INCREMENT PRIMARY KEY,
  mz INT NOT NULL,
  intensity INT NOT NULL,
  gcms_spectrum_id INT NOT NULL,
  FOREIGN KEY (gcms_spectrum_id) REFERENCES gcms_spectrum(gcms_spectrum_id) ON DELETE CASCADE
);

-- create table GC_RI_RT
CREATE TABLE gc_ri_rt (
  gc_ri_rt_id INT PRIMARY KEY AUTO_INCREMENT,
  RI INT NOT NULL,
  RT INT NOT NULL,
  compound_id INT NOT NULL,
  derivatization_method_id INT NOT NULL,
  FOREIGN KEY (compound_id) REFERENCES compounds_derivatizationmethods(compound_id) ON DELETE CASCADE,
  FOREIGN KEY (derivatization_method_id) REFERENCES derivatization_methods(derivatization_id) ON DELETE CASCADE
);

-- create table GC Column (1-n with GC_RI_RT)
CREATE TABLE gc_column(
  gc_column_id INT AUTO_INCREMENT PRIMARY KEY,
  gc_ri_rt_id INT NOT NULL,
  gc_column_name VARCHAR(255),
  FOREIGN KEY (gc_ri_rt_id) REFERENCES gc_ri_rt(gc_ri_rt_id) ON DELETE CASCADE
);