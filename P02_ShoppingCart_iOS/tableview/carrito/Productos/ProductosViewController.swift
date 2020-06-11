//
//  ViewController.swift
//  carrito
//
//  Created by Denisse Maldonado on 4/30/20.
//  Copyright © 2020 tecdam. All rights reserved.
//

import UIKit

class ProductosViewController: UIViewController {
    @IBOutlet weak var tableProductos: UITableView!
    
    var names = [String]()
    var precios = [String]()
    var data = [String: String]()
    var precioItem: String = "0"
    var nombreItem: String = "nombre"
    var precioTotal: Int = 0
    var cantidadProductos: Int = 0
    var obtenerDatosParaCartView: Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        
        if let productos = UserDefaults.standard.dictionary(forKey: "productos") {
            
            self.data = productos as! [String: String]
            
            for(name, price) in self.data {
                names.append(name)
                precios.append(price)
            }
            
            if obtenerDatosParaCartView {
                obtenerDatosParaCartView = false
                for i in 0...precios.count-1 {
                    precioTotal += Int(precios[i])!
                }
                cantidadProductos = data.count
            }
            
            tableProductos.reloadData()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toItemView" {
            let viewControllerDestino = segue.destination as! itemView
            viewControllerDestino.nombre = nombreItem
            viewControllerDestino.precio = precioItem
            viewControllerDestino.PRECIO = precioTotal
            viewControllerDestino.CANTIDAD = cantidadProductos
        }
        else if segue.identifier == "toCartView" {
            let viewControllerDestino = segue.destination as! cartView
            viewControllerDestino.PRECIO = precioTotal
            viewControllerDestino.CANTIDAD = cantidadProductos
        }
    }

}

extension ProductosViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "productoCell", for: indexPath) as! PCTableViewCell
        
        cell.nombreLabel.text = names[indexPath.row]
        cell.priceLabel.text = precios[indexPath.row]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.dequeueReusableCell(withIdentifier: "productoCell", for: indexPath) as! PCTableViewCell
        tableView.deselectRow(at: indexPath, animated: true)
        nombreItem = String(names[indexPath.row])
        precioItem = String(precios[indexPath.row])
        performSegue(withIdentifier: "toItemView", sender: cell)
    }
    
}
