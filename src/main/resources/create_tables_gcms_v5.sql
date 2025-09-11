
-- derivatization method
CREATE TABLE derivatization_methods (
  derivatization_method_id int AUTO_INCREMENT PRIMARY KEY,
  derivatization_type varchar(255)
);

-- creates table gcms spectrum
CREATE TABLE gcms_spectrum (
  gcms_spectrum_id INT AUTO_INCREMENT PRIMARY KEY,
  compound_id INT NOT NULL,
  derivatization_method_id INT NOT NULL,
  FOREIGN KEY (compound_id) REFERENCES compounds(compound_id) ON DELETE CASCADE,
  FOREIGN KEY (derivatization_method_id) REFERENCES derivatization_methods(derivatization_method_id) ON DELETE CASCADE
);

-- creates table gcms peak
CREATE TABLE gcms_peaks (
  gcms_peaks_id INT AUTO_INCREMENT PRIMARY KEY,
  gcms_spectrum_id INT NOT NULL,
  mz DOUBLE NOT NULL,
  intensity DOUBLE NOT NULL,
  FOREIGN KEY (gcms_spectrum_id) REFERENCES gcms_spectrum(gcms_spectrum_id) ON DELETE CASCADE
);

-- create table GC_RI_RT
CREATE TABLE gc_ri_rt (
  gc_ri_rt_id INT PRIMARY KEY AUTO_INCREMENT,
  compound_id INT NOT NULL,
  derivatization_method_id INT NOT NULL,
  gc_column_id INT NOT NULL,
  RI DOUBLE NOT NULL,
  RT DOUBLE DEFAULT NULL,
  FOREIGN KEY (compound_id) REFERENCES compounds(compound_id) ON DELETE CASCADE,
  FOREIGN KEY (derivatization_method_id) REFERENCES derivatization_methods(derivatization_method_id) ON DELETE CASCADE,
  FOREIGN KEY (gc_column_id) REFERENCES gc_column(gc_column_id) ON DELETE CASCADE, 
  UNIQUE (compound_id, derivatization_method_id, gc_column_id)
);

-- create table GC Column
CREATE TABLE gc_column(
  gc_column_id INT AUTO_INCREMENT PRIMARY KEY,
  gc_column_name VARCHAR(255)
);