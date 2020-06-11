//
//  cartView.swift
//  carrito
//
//  Created by itzel naranjo🧸 on 08/05/20.
//  Copyright © 2020 tecdam. All rights reserved.
//

import Foundation
import UIKit

class cartView: UIViewController {
    
    var PRECIO = 0
    var CANTIDAD = 0
    @IBOutlet var etiquetaPrecio: UILabel!
    @IBOutlet var etiquetaCantidadProductos: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        etiquetaPrecio.text = "$ " + String(PRECIO) + ".00"
        etiquetaCantidadProductos.text = String(CANTIDAD)
    }

}
