import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageItemComponent } from './image-item.component';

describe('ImageItemComponent', () => {
  let component: ImageItemComponent;
  let fixture: ComponentFixture<ImageItemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImageItemComponent]
    });
    fixture = TestBed.createComponent(ImageItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
