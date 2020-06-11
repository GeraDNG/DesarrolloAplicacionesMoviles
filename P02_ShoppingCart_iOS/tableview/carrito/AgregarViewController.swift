//
//  AgregarViewController.swift
//  carrito
//
//  Created by Denisse Maldonado on 4/30/20.
//  Copyright © 2020 tecdam. All rights reserved.
//

import UIKit

class AgregarViewController: UIViewController {
    @IBOutlet weak var nombreText: UITextField!
    @IBOutlet weak var precioText: UITextField!
    @IBOutlet weak var btnImage: UIButton!
    
    var imagenProducto: Data!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func agregarAction(_ sender: Any) {
        agregarProducto()
    }
    
    @IBAction func takePhoto(_ sender: Any) {
        let sheet = UIAlertController(title: "Escoge una Opción", message: "Foto o Galería", preferredStyle: .actionSheet)
        let camera = UIAlertAction(title: "Camara", style: .default) { (action: UIAlertAction) in
            
            if(UIImagePickerController.isSourceTypeAvailable(.camera)) {
                let picker = UIImagePickerController()
                picker.sourceType = .camera
                picker.delegate = self
                
                if(UIDevice.current.userInterfaceIdiom == .pad) {
                    picker.modalPresentationStyle = .popover
                    picker.popoverPresentationController?.sourceView = self.btnImage
                }
                
                self.present(picker, animated: true, completion: nil)
            } else {
                let alert = UIAlertController(title: "Error", message: "Tu dispositivo no cuenta con cámara", preferredStyle: .alert)
                let ok = UIAlertAction(title: "Ok", style: .default, handler: nil)
                alert.addAction(ok)
                self.present(alert, animated: true, completion: nil)
            }
        }
        
        let galeria = UIAlertAction(title: "Galería", style: .default) { (action:UIAlertAction) in
            
            let picker = UIImagePickerController()
            picker.sourceType = .photoLibrary
            picker.delegate = self
            
            self.present(picker, animated: true, completion: nil)
        }
        
        sheet.addAction(camera)
        sheet.addAction(galeria)
        present(sheet, animated: true, completion: nil)
    }
    
    func agregarProducto() {
        let nombre = nombreText.text!
        let precio = precioText.text!
        
        if let dict = UserDefaults.standard.dictionary(forKey: "productos") {
            var productos = dict
            productos["\(nombre)"] = "\(precio)"
            UserDefaults.standard.set(productos, forKey: "productos")
        } else {
            let productos = ["\(nombre)": "\(precio)"]
            UserDefaults.standard.set(productos, forKey: "productos")
        }
        
        let alert = UIAlertController(title: "Éxito", message: "Se ha agregado el producto", preferredStyle: .alert)
        let ok = UIAlertAction(title: "Ok", style: .default, handler: nil)
        let regresar = UIAlertAction(title: "Regresar", style: .cancel) { (action:UIAlertAction) in
            self.navigationController?.popViewController(animated: true)
        }
        alert.addAction(ok)
        alert.addAction(regresar)
        present(alert, animated: true, completion: nil)
    }
}

extension  AgregarViewController: UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        print("Cancel")
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        let imagen = info[.originalImage] as! UIImage
        imagenProducto = imagen.jpegData(compressionQuality: 1.0)
        btnImage.setImage(imagen, for: .normal)
    }
}

extension AgregarViewController: UITextFieldDelegate {
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if(nombreText == textField) {
            precioText.becomeFirstResponder()
            return false
        } else {
            agregarProducto()
            return true
        }
    }
}
