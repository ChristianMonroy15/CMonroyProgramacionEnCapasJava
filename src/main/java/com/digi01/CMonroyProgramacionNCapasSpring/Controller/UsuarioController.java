package com.digi01.CMonroyProgramacionNCapasSpring.Controller;

import com.digi01.CMonroyProgramacionNCapasSpring.DAO.ColoniaDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.DireccionDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.EstadoDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.MunicipioDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.PaisDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.RolDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.DAO.UsuarioDAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.ErrorCarga;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Rol;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import com.digi01.CMonroyProgramacionNCapasSpring.Service.ValidationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @Autowired
    private DireccionDAOImplementation direccionDAOImplementation;

    @Autowired
    private RolDAOImplementation rolDAOImplementation;

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;

    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;

    @GetMapping("estado/{idPais}")
    @ResponseBody //Retorna dato Estucturado
    public Result GetEstadosByIdPais(@PathVariable("idPais") int idPais) {
        return estadoDAOImplementation.GetByIdPais(idPais);
    }

    @GetMapping("municipio/{idEstado}")
    @ResponseBody
    public Result GetMunicipiosByIdEstado(@PathVariable("idEstado") int idEstado) {
        return municipioDAOImplementation.GetByIdEstado(idEstado);
    }

    @GetMapping("colonia/{idMunicipio}")
    @ResponseBody
    public Result GetColoniasByIdMunicipio(@PathVariable("idMunicipio") int idMunicipio) {
        return coloniaDAOImplementation.GetByIdMunicipio(idMunicipio);
    }

    @GetMapping()
    public String Index(Model model) {
        Result result = usuarioDAOImplementation.GetAll();
        model.addAttribute("usuarios", result.objects);
        model.addAttribute("paises", paisDAOImplementation);
        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);

        model.addAttribute("usuariosBusqueda", new Usuario());
        return "UsuarioIndex";
    }

    @GetMapping("add")
    public String Add(Model model) {
        model.addAttribute("Usuario", new Usuario());
        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
        model.addAttribute("paises", paisDAOImplementation.GetAll().objects);
        return "UsuarioForm";
    }

    @GetMapping("{detail}")
    public String Detail(@PathVariable("detail") int detail, Model model) {

        Result result = usuarioDAOImplementation.GetById(detail);

        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
        model.addAttribute("usuario", result.object);
        model.addAttribute("Direccion", new Direccion());
        model.addAttribute("paises", paisDAOImplementation.GetAll().objects);

        return "UsuarioDetail";
    }

    @GetMapping("/direccion/{idDireccion}")
    @ResponseBody
    public Direccion getDireccion(@PathVariable int idDireccion) {
        return (Direccion) direccionDAOImplementation.GetById(idDireccion).object;
    }

    @GetMapping("deleteUsuario/{idUsuario}")
    public String DeleteUsuario(@PathVariable("idUsuario") int idUsuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Result result = usuarioDAOImplementation.Delete(idUsuario);

        redirectAttributes.addFlashAttribute("resultDelete", result);
        return "redirect:/usuario";
    }

    @PostMapping("addDireccion/{idUsuario}")
    public String AddDireccion(@ModelAttribute("Direccion") Direccion direccion,
            Model model,
            @PathVariable("idUsuario") int idUsuario,
            RedirectAttributes redirectAttributes) {

        if (direccion.getIdDireccion() > 0) {
            Result result = direccionDAOImplementation.Update(direccion);
            if (result.correct == true) {
                redirectAttributes.addFlashAttribute("successMessage", "Se Actualizo la direccion correctamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se Actualizo la direccion");
            }
        }
        if (direccion.getIdDireccion() == 0) {
            Result result = direccionDAOImplementation.Add(direccion, idUsuario);
            if (result.correct == true) {
                redirectAttributes.addFlashAttribute("successMessage", "Se agrego la direccion correctamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se agrego la direccion");
            }
            redirectAttributes.addFlashAttribute("resultAdd", result);
        }

        return "redirect:/usuario/" + idUsuario;
    }

    @GetMapping("/cargamasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @GetMapping("/cargamasiva/procesar")
    public String CargaMasiva(HttpSession session) {
        String Path = session.getAttribute("archivoCargaMasiva").toString();
        session.removeAttribute("archivoCargaMasiva");

        //inserción con carga masiva
        // 
        return "CargaMasiva";
    }

    @PostMapping("/cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
            HttpSession session,
            Model model) {

        String extension = archivo.getOriginalFilename().split("\\.")[1];

        String path = System.getProperty("user.dir");
        String pathArchivo = "src/main/resources/archivosCarga";
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
        String pathDefinitvo = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();

        try {
            archivo.transferTo(new File(pathDefinitvo));

        } catch (Exception ex) {
            String errortransferencia = ex.getLocalizedMessage();
        }

        List<Usuario> usuarios;
        if (extension.equalsIgnoreCase("txt")) {
            usuarios = LecturaArchivoTXT(new File(pathDefinitvo));

        } else if (extension.equalsIgnoreCase("xlsx")) {
            usuarios = LecturaArchivoXLSX(new File(pathDefinitvo));

        } else {
            // error de archivo
            model.addAttribute("errroMessage", "Manda un archivo que sea valido :@");
            return "CargaMasiva";
        }
        List<ErrorCarga> errores = ValidarDatosArchivo(usuarios);
        if (errores.isEmpty()) {
            //Boton de procesar
            model.addAttribute("error", false);
            session.setAttribute("archivoCargaMasiva", pathDefinitvo);
        } else {
            //Lista Errores
            model.addAttribute("error", true);
            model.addAttribute("errores", errores);
        }
        return "CargaMasiva";
    }

    public List<Usuario> LecturaArchivoTXT(File archivo) {

        List<Usuario> usuarios = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(archivo); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {

            String linea = "";

            while ((linea = bufferedReader.readLine()) != null) {

                String[] campos = linea.split("\\|");
                Usuario usuario = new Usuario();

                usuario.setNombre(campos[0].trim());
                usuario.setApellidoPaterno(campos[1].trim());
                usuario.setApellidoMaterno(campos[2].trim());
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = campos[3].trim();
                Date fecha2 = formato.parse(fecha);
                usuario.setFechaNacimiento(fecha2);
                usuario.setTelefono(campos[4].trim());
                usuario.setUsername(campos[5].trim());
                usuario.setEmail(campos[6].trim());
                usuario.setPassword(campos[7].trim());
                usuario.setSexo(campos[8].trim());
                usuario.setCelular(campos[9].trim());
                usuario.setCurp(campos[10].trim());
                usuario.setRol(new Rol());
                usuario.Rol.setIdRol(Integer.parseInt(campos[11].trim()));

                usuarios.add(usuario);
            }

        } catch (Exception ex) {
            return null;
        }
        return usuarios;
    }

    public List<Usuario> LecturaArchivoXLSX(File archivo) {

        List<Usuario> usuarios = new ArrayList<>();

        try (InputStream fileInputStream = new FileInputStream(archivo); XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
            XSSFSheet workSheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (Row row : workSheet) {

                Usuario usuario = new Usuario();

                usuario.setNombre(formatter.formatCellValue(
                        row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setApellidoPaterno(formatter.formatCellValue(
                        row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setApellidoMaterno(formatter.formatCellValue(
                        row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );

                // Fecha con formato de exel
                Cell fechaCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (fechaCell.getCellType() == CellType.NUMERIC) {
                    usuario.setFechaNacimiento(fechaCell.getDateCellValue());
                }

                usuario.setTelefono(formatter.formatCellValue(
                        row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setUsername(formatter.formatCellValue(
                        row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setEmail(formatter.formatCellValue(
                        row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setPassword(formatter.formatCellValue(
                        row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setSexo(formatter.formatCellValue(
                        row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setCelular(formatter.formatCellValue(
                        row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );
                usuario.setCurp(formatter.formatCellValue(
                        row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
                );

                usuario.Rol = new Rol();
                usuario.Rol.setIdRol(
                        (int) row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                .getNumericCellValue()
                );

                usuarios.add(usuario);

            }

        } catch (Exception ex) {
            return null;

        }
        return usuarios;
    }

    public List<ErrorCarga> ValidarDatosArchivo(List<Usuario> usuarios) {
        List<ErrorCarga> erroresCarga = new ArrayList();

        int lineaError = 0;

        for (Usuario usuario : usuarios) {
            lineaError++;
            BindingResult bindingResult = validationService.validateObject(usuario);
            List<ObjectError> errors = bindingResult.getAllErrors();

            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;
                ErrorCarga errorCarga = new ErrorCarga();
                errorCarga.campo = fieldError.getField();
                errorCarga.descripcion = fieldError.getDefaultMessage();
                errorCarga.linea = lineaError;
                erroresCarga.add(errorCarga);
            }
        }
        return erroresCarga;
    }

    @PostMapping("/imagen/update")
    public String actualizarImagen(@RequestParam int idUsuario,
            @RequestParam String imagen) {
        if (imagen.contains(",")) {
            imagen = imagen.split(",")[1];
        }
        usuarioDAOImplementation.UpdateImagen(idUsuario, imagen);
        return "redirect:/usuario/" + idUsuario;
    }

    @PostMapping("/detail")
    public String UpdateUsuario(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirectAttributes) {

        Result result = usuarioDAOImplementation.Update(usuario);

        if (result.correct == true) {
            redirectAttributes.addFlashAttribute("successMessage", "Se actualizo la informacion del usuario " + usuario.getUsername());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Se actualizo la informacion del usuario " + usuario.getUsername());
        }
        return "redirect:/usuario/" + usuario.getIdUsuario();
    }

    @PostMapping("add")
    public String Add(@Valid @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("imagenFile") MultipartFile imagenFile) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("Usuario", usuario); // colonia tiene, municipio, estado y que pais
            model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
            model.addAttribute("paises", paisDAOImplementation.GetAll().objects);

            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
                model.addAttribute("estados", estadoDAOImplementation.GetByIdPais(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);
                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
                    model.addAttribute("municipios", municipioDAOImplementation.GetByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);
                    if (usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio() > 0) {
                        model.addAttribute("colonias", coloniaDAOImplementation.GetByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
                    }
                }
            }
            redirectAttributes.addFlashAttribute("errorMessageAdd", "Revisa que los sean campos validos y esten completos");

            return "UsuarioForm";

        }

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {

                String nombreArchivo = imagenFile.getOriginalFilename();

                // Validamos que no venga null y que contenga punto
                if (nombreArchivo != null && nombreArchivo.contains(".")) {

                    String[] partes = nombreArchivo.split("\\.");
                    String extension = partes[partes.length - 1]; // último segmento

                    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
                        byte[] byteImagen = imagenFile.getBytes();
                        String imagenBase64 = Base64.getEncoder().encodeToString(byteImagen);
                        usuario.setImagen(imagenBase64);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Result result = usuarioDAOImplementation.Add(usuario);
        redirectAttributes.addFlashAttribute("successMessageAdd", "El usuario " + usuario.getUsername() + "se creo con exito.");
        return "redirect:/usuario";
    }

    @PostMapping("deleteDireccion")
    public String DeleteDireccion(int IdDireccion,
            @RequestParam("IdUsuario") int IdUsuario,
            RedirectAttributes redirectAttributes) {

        Result result = direccionDAOImplementation.Delete(IdDireccion);

        return "redirect:/usuario/" + IdUsuario;
    }

    @PostMapping()
    public String GetAllDinamico(@ModelAttribute("usuariosBusqueda") Usuario usuario, Model model) {
        Result result = usuarioDAOImplementation.GetAllDinamico(usuario);

        model.addAttribute("usuarios", result.objects);
        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
        model.addAttribute("usuariosBusqueda", usuario);

        return "UsuarioIndex";
    }

}
