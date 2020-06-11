//
//  itemView.swift
//  carrito
//
//  Created by itzel naranjo🧸 on 08/05/20.
//  Copyright © 2020 tecdam. All rights reserved.
//

import Foundation
import UIKit

class itemView: UIViewController {
    
    var PRECIO = 0
    var CANTIDAD = 0
    var precio: String = "0"
    var nombre: String = "nombre"
    
    @IBOutlet var etiquetaNombre: UILabel!
    @IBOutlet var etiquetaPrecio: UILabel!
    @IBOutlet var etiquetaContador: UILabel!
    
    @IBAction func cambioContador(_ sender: UIStepper) {
        etiquetaContador.text = String(Int(sender.value))
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "fromItemViewToCartView" {
            let viewControllerDestino = segue.destination as! cartView
            viewControllerDestino.CANTIDAD = CANTIDAD
            viewControllerDestino.PRECIO = PRECIO
        }
    }
    
    @IBAction func mostrarCarrito(_ sender: UIBarButtonItem) {
        self.performSegue(withIdentifier: "fromItemViewToCartView", sender: self)
    }
    
    @IBAction func mostrarShare(_ sender: UIBarButtonItem) {
        let menuCompartir = UIActivityViewController(activityItems: [nombre], applicationActivities: nil)
        present(menuCompartir, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        etiquetaNombre.text = nombre
        etiquetaPrecio.text = "$ " + precio + ".00"
    }
}
