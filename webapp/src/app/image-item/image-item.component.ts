import { Component, Input } from '@angular/core';
import { Image } from '../interfaces/image';

@Component({
  selector: 'app-image-item',
  templateUrl: './image-item.component.html',
  styleUrls: ['./image-item.component.css']
})
export class ImageItemComponent {
  @Input() image!: Image;
}
